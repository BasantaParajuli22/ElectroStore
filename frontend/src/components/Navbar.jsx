import { useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { useCart } from "../contexts/CartContext"; // Import useCart to get cartCount
import AuthModal from "./AuthModal";
import "./Navbar.css";


function Navbar() {
  // Destructure 'user' to get the 'role' property, along with other auth states
  const { user, logout, isAuthenticated, isLoading } = useAuth();
  const { cartCount } = useCart(); // Get cartCount from CartContext

  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);
  const [authModalMode, setAuthModalMode] = useState("login");

  const toggleMenu = () => setIsMenuOpen(!isMenuOpen);
  const toggleDropdown = () => setIsDropdownOpen(!isDropdownOpen);

  const openLoginModal = () => {
    setAuthModalMode("login");
    setIsAuthModalOpen(true);
  };

  const openRegisterModal = () => {
    setAuthModalMode("register");
    setIsAuthModalOpen(true);
  };

  // Determine if the user is truly logged in based on token validity
  const isLoggedIn = !isLoading && isAuthenticated();
  // Determine user's role for conditional rendering
  const userRole = user?.role; // Access the role from the user object

  return (
    <header className="navbar">
      <div className="navbar-container">
        <div className="navbar-content">
          {/* Logo */}
          <Link to="/" className="navbar-logo">
            <span className="logo-icon">⚡</span>
            <span className="logo-text">ElectroMart</span>
          </Link>

          {/* Desktop Navigation */}
          <nav className="navbar-nav">
            <Link to="/" className="nav-link">Home</Link>
            <Link to="/about" className="nav-link">About</Link>
            <Link to="/contact" className="nav-link">Contact</Link>
            {/* Show Shop link for all logged-in users, or for everyone if guests can browse */}
            <Link to="/shop" className="nav-link">Shop</Link>
            {/* Admin-specific link */}
            {isLoggedIn && userRole === 'ADMIN' && (
              <Link to="/dashboard" className="nav-link">Dashboard</Link>
            )}
          </nav>

          {/* Auth & Cart Buttons */}
          <div className="navbar-actions">
            {isLoading ? ( // Show loading state if auth status is still being determined
              <div className="loading-state">Loading...</div>
            ) : isLoggedIn ? ( // Check if isLoggedIn
              <>
                {/* Cart Button for CUSTOMERS */}
                {userRole === 'CUSTOMER' && (
                  <Link to="/cart" className="cart-btn">
                    🛒 Cart {cartCount > 0 && <span className="cart-count">{cartCount}</span>}
                  </Link>
                )}

                <div className="user-dropdown">
                  <button onClick={toggleDropdown} className="user-btn">
                    {user?.username || "My Account"}
                  </button>
                  {isDropdownOpen && (
                    <div className="dropdown-menu">
                      {/* Profile and Orders for CUSTOMER */}
                      {userRole === 'CUSTOMER' && (
                        <>
                          <Link to="/profile" className="dropdown-item">Profile</Link>
                          <Link to="/orders" className="dropdown-item">Orders</Link>
                          <hr className="dropdown-divider" />
                        </>
                      )}
                      {/* Admin might just have a logout, or specific admin links */}
                      <button onClick={logout} className="dropdown-item logout-btn">
                        Logout
                      </button>
                    </div>
                  )}
                </div>
              </>
            ) : (
              <>
                <button onClick={openLoginModal} className="login-btn">Login</button>
                <button onClick={openRegisterModal} className="register-btn">Register</button>
              </>
            )}

            {/* Mobile Menu Button */}
            <button onClick={toggleMenu} className="mobile-menu-btn">
              ☰
            </button>
          </div>
        </div>

        {/* Mobile Menu (Conditionally render based on role) */}
        {isMenuOpen && (
          <div className="mobile-menu">
            <Link to="/" className="mobile-nav-link" onClick={toggleMenu}>Home</Link>
            <Link to="/about" className="mobile-nav-link" onClick={toggleMenu}>About</Link>
            <Link to="/contact" className="mobile-nav-link" onClick={toggleMenu}>Contact</Link>
            <Link to="/shop" className="mobile-nav-link" onClick={toggleMenu}>Shop</Link>

            {isLoggedIn && userRole === 'ADMIN' && (
              <Link to="/dashboard" className="mobile-nav-link" onClick={toggleMenu}>Dashboard</Link>
            )}

            {isLoggedIn && userRole === 'CUSTOMER' && (
              <>
                <Link to="/cart" className="mobile-nav-link" onClick={toggleMenu}>
                  🛒 Cart {cartCount > 0 && <span className="cart-count">{cartCount}</span>}
                </Link>
                <Link to="/profile" className="mobile-nav-link" onClick={toggleMenu}>Profile</Link>
                <Link to="/orders" className="mobile-nav-link" onClick={toggleMenu}>Orders</Link>
              </>
            )}
            {isLoggedIn && (
                <button onClick={() => { logout(); toggleMenu(); }} className="mobile-nav-link logout-btn">
                    Logout
                </button>
            )}
            {!isLoggedIn && (
              <>
                <button onClick={() => { openLoginModal(); toggleMenu(); }} className="mobile-nav-link login-btn">Login</button>
                <button onClick={() => { openRegisterModal(); toggleMenu(); }} className="mobile-nav-link register-btn">Register</button>
              </>
            )}
          </div>
        )}
      </div>
      <AuthModal isOpen={isAuthModalOpen} onClose={() => setIsAuthModalOpen(false)} initialMode={authModalMode} />
    </header>
  );
}

export default Navbar;