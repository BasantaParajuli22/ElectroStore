import { useState, useEffect } from 'react';
import './App.css';
import { Route, Routes, useNavigate } from 'react-router-dom';
import Layout from './pages/layout/Layout';
import Home from './pages/home/Home';
import Contact from './pages/home/Contact';
import About from './pages/home/About';
import ProductDetails from './components/products/ProductDetails';
import Products from './components/products/Products';
import ProductManager from './components/products/ProductManager';
import Login from './pages/layout/Login';
import Register from './pages/home/Register';
import CartPage from './pages/cart/CartPage';

function App() {
  const [user, setUser] = useState(null);
  const [jwtToken, setJwtToken] = useState(() => localStorage.getItem('jwtToken'));
  const [cartCount, setCartCount] = useState(0);
  const navigate = useNavigate(); // useNavigate can now be used as App is within the Router context (defined in main.jsx)

  const handleLogin = (data) => {
    setUser({ email: data.email, roles: data.roles, id: data.id, username: data.username }); // Store basic user info
    setJwtToken(data.token);
    localStorage.setItem('jwtToken', data.token);
    fetchCartCount(data.token);
  };

  const handleLogout = () => {
    setUser(null);
    setJwtToken(null);
    setCartCount(0);
    localStorage.removeItem('jwtToken');
  };

  const handleRegister = (userData) => {
    // Assuming your registration endpoint returns the same data as login
    handleLogin(userData);
  };

  const fetchCartCount = async (token) => {
    if (!token) return;
    try {
      const response = await fetch('http://localhost:8080/api/users/cart/count', {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      if (response.ok) {
        const data = await response.json();
        setCartCount(data.count);
      } else if (response.status === 401) {
        // Token might be invalid, log out user
        handleLogout();
        navigate('/login');
      } else {
        console.error('Failed to fetch cart count:', response.status);
      }
    } catch (error) {
      console.error('Failed to fetch cart count:', error);
    }
  };

  const updateCartCount = (change) => {
    setCartCount(prev => prev + change);
  };

  useEffect(() => {
    // Initial check for JWT on component mount
    if (jwtToken) {
      // Optionally, you could fetch user details here if needed
      // For now, we just fetch the cart count
      fetchCartCount(jwtToken);
    }
  }, [jwtToken, navigate]);

  return (
    <Routes>
      <Route path='/' element={<Layout user={user} cartCount={cartCount} onLogout={handleLogout} />}>
        <Route index element={<Home />} /> {/* Use index for the default child route */}
        <Route path='/shop' element={<Products user={user} updateCartCount={updateCartCount} />} />
        <Route path='/products/:id' element={<ProductDetails user={user} updateCartCount={updateCartCount} />} />
        <Route path='/products/manage' element={<ProductManager />} />
        <Route path='/contact' element={<Contact />} />
        <Route path='/about' element={<About />} />
        <Route path="/login" element={<Login onLogin={handleLogin} />} />
        <Route path="/register" element={<Register onRegister={handleRegister} />} />
        <Route
          path="/cart"
          element={jwtToken ? <CartPage user={user} updateCartCount={updateCartCount} /> : <Login onLogin={handleLogin} />}
        />
      </Route>
    </Routes>
  );
}

export default App;