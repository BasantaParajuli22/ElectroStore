import React, { useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import './styles/Navbar.css';

const Navbar = ({ user, cartCount, onLogout }) => {
  const [showDropdown, setShowDropdown] = useState(false);
  const navigate = useNavigate();

  const handleAuthClick = () => {
    if (user) {
      setShowDropdown(!showDropdown);
    } else {
      navigate('/login');
    }
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <NavLink to="/" className="navbar-logo">ElectroStore</NavLink>

        <div className="navbar-links">
          <NavLink to="/" className="navbar-link">Home</NavLink>
          <NavLink to="/shop" className="navbar-link">Shop</NavLink>
          <NavLink to="/about" className="navbar-link">About</NavLink>
          <NavLink to="/contact" className="navbar-link">Contact</NavLink>

          <div className="navbar-auth-section">
            {user ? (
              <div className="user-dropdown">
                <button
                  className="navbar-auth-btn"
                  onClick={handleAuthClick}
                >
                  {user.email}  {/* Use email consistently */}
                </button>
                {showDropdown && (
                  <div className="dropdown-menu">
                    <NavLink to="/profile" className="dropdown-item">Profile</NavLink>
                    <button
                      className="dropdown-item"
                      onClick={onLogout}
                    >
                      Logout
                    </button>
                  </div>
                )}
              </div>
            ) : (
              <>
                <NavLink to="/login" className="navbar-auth-btn">Login</NavLink>
                <span className="auth-separator">|</span>
                <NavLink to="/register" className="navbar-auth-btn">Register</NavLink>
              </>
            )}

            <NavLink to="/cart" className="navbar-cart-btn">
              Cart ({cartCount || 0})
            </NavLink>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;