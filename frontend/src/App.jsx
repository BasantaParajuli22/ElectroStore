import { useState, useEffect } from 'react';
import './App.css';
import { Route, RouterProvider, createBrowserRouter, createRoutesFromElements, useNavigate } from 'react-router-dom';
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
  const [user, setUser] = useState(() => {
    const savedUser = localStorage.getItem('user');
    return savedUser ? JSON.parse(savedUser) : null;
  });
  const [cartCount, setCartCount] = useState(0);

  const handleLogin = (userData) => {
    setUser(userData);
    localStorage.setItem('user', JSON.stringify(userData));
    fetchCartCount(userData.email);
  };

  const handleLogout = () => {
    setUser(null);
    setCartCount(0);
    localStorage.removeItem('user');
  };

  const handleRegister = (userData) => {
    handleLogin(userData);
  };

  const fetchCartCount = async (email) => {
    try {
      const response = await fetch(`http://localhost:8080/api/users/cart/count?email=${email}`);
      if (response.ok) {
        const data = await response.json();
        setCartCount(data.count);
      }
    } catch (error) {
      console.error('Failed to fetch cart count:', error);
    }
  };

  const updateCartCount = (change) => {
    setCartCount(prev => prev + change);
  };

  useEffect(() => {
    if (user) {
      localStorage.setItem('user', JSON.stringify(user));
    } else {
      localStorage.removeItem('user');
    }
  }, [user]);

  // Add this effect to check auth status on initial load
  useEffect(() => {
    if (user) {
      fetchCartCount(user.email);
    }
  }, []);

  const router = createBrowserRouter(
    createRoutesFromElements(
      <Route path='/' element={<Layout user={user} cartCount={cartCount} onLogout={handleLogout} />}>
        <Route path='/' element={<Home />} />
        <Route path='/shop' element={<Products user={user} updateCartCount={updateCartCount} />} />
        <Route path='/products/:id' element={<ProductDetails user={user} updateCartCount={updateCartCount} />} />
        <Route path='/products/manage' element={<ProductManager />} />
        <Route path='/contact' element={<Contact />} />
        <Route path='/about' element={<About />} />
        <Route path="/login" element={<Login onLogin={handleLogin} />} />
        <Route path="/register" element={<Register onRegister={handleRegister} />} />
        <Route
          path="/cart"
          element={user ? <CartPage user={user} updateCartCount={updateCartCount} /> : <Login onLogin={handleLogin} />}
        />
      </Route>
    )
  );

  return <RouterProvider router={router} />;
}

export default App;