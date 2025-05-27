import { AuthProvider } from "./contexts/AuthContext";
import { CartProvider } from "./contexts/CartContext"; // Import CartProvider
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import { Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import About from "./pages/About";
import Contact from "./pages/Contact";
import ShoppingPage from "./pages/ShoppingPage"; // Import ShoppingPage
import ProfilePage from "./pages/ProfilePage";
import OrdersPage from "./pages/OrdersPage";
import "./App.css";
import ProductDetailsPage from "./pages/ProductDetailsPage";

function App() {
  return (
    <AuthProvider>
      {/* Wrap everything that needs cart context inside CartProvider */}
      <CartProvider>
        <div className="app">
          <Navbar /> {/* Navbar might also need CartContext for cart count */}
          <main className="main-content">
            <Routes>
              <Route path="/" element={<Home />} />
              {/* <Route path="/login" element={< />} /> */}
              <Route path="/about" element={<About />} />
              <Route path="/contact" element={<Contact />} />
              <Route path="/profile" element={<ProfilePage />} />
              <Route path="/orders" element={<OrdersPage />} />
              <Route path="/shop" element={<ShoppingPage />} />
              <Route path="/products/:id" element={<ProductDetailsPage />} />

            </Routes>
          </main>
          <Footer />
        </div>
      </CartProvider>
    </AuthProvider>
  );
}

export default App;