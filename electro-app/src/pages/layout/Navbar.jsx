import React from 'react'
import { NavLink } from 'react-router-dom';

import './styles/Navbar.css';

const Navbar = () => {
  return (
    <nav className="navbar">
      <div className="navbar-container">
        <NavLink to="/" className="navbar-logo">ElectroStore</NavLink>
        <div className="navbar-links">
          <NavLink to="/" className="navbar-link">Home</NavLink>
          <NavLink to="/shop" className="navbar-link">Shop</NavLink>
          <NavLink to="/about" className="navbar-link">About</NavLink>
          <NavLink to="/contact" className="navbar-link">Contact</NavLink>
          <button className="navbar-cart-btn">
            Cart (0)
          </button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;