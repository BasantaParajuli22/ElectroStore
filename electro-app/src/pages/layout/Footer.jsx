import React from 'react';
import { Link } from 'react-router-dom';
import './styles/Footer.css';

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-section">
          <h3 className="footer-title">ElectroStore</h3>
          <p>Your one-stop shop for the latest electronics and gadgets.</p>
        </div>
        <div className="footer-section">
          <h4 className="footer-subtitle">Quick Links</h4>
          <ul className="footer-links">
            <li><Link to="/" className="footer-link">Home</Link></li>
            <li><Link to="/about" className="footer-link">About Us</Link></li>
            <li><Link to="/contact" className="footer-link">Contact</Link></li>
          </ul>
        </div>
        <div className="footer-section">
          <h4 className="footer-subtitle">Contact Info</h4>
          <p>123 Tech Street</p>
          <p>Electro City, EC 10001</p>
          <p>Phone: (123) 456-7890</p>
          <p>Email: info@electrostore.com</p>
        </div>
      </div>
      <div className="footer-bottom">
        <p>&copy; {new Date().getFullYear()} ElectroStore. All rights reserved.</p>
      </div>
    </footer>
  );
};

export default Footer;