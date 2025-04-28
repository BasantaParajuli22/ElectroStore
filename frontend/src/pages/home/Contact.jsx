import React from 'react';
import './styles/Contact.css';

const Contact = () => {
  return (
    <div className="contact-container">
      <section className="contact-section">
        <h1 className="contact-title">Contact Us</h1>
        <div className="contact-grid">
          <div className="contact-form-container">
            <h2 className="form-title">Get in Touch</h2>
            <form className="contact-form">
              <div className="form-group">
                <label htmlFor="name">Name</label>
                <input
                  type="text"
                  id="name"
                  placeholder="Your Name"
                />
              </div>
              <div className="form-group">
                <label htmlFor="email">Email</label>
                <input
                  type="email"
                  id="email"
                  placeholder="Your Email"
                />
              </div>
              <div className="form-group">
                <label htmlFor="message">Message</label>
                <textarea
                  id="message"
                  rows="4"
                  placeholder="Your Message"
                ></textarea>
              </div>
              <button type="submit" className="submit-btn">
                Send Message
              </button>
            </form>
          </div>
          <div className="contact-info-container">
            <h2 className="info-title">Contact Information</h2>
            <div className="contact-info">
              <div className="info-item">
                <h3>Address</h3>
                <p>123 Tech Street, Electro City, EC 10001</p>
              </div>
              <div className="info-item">
                <h3>Phone</h3>
                <p>(123) 456-7890</p>
              </div>
              <div className="info-item">
                <h3>Email</h3>
                <p>info@electrostore.com</p>
              </div>
              <div className="info-item">
                <h3>Business Hours</h3>
                <p>Monday - Friday: 9am - 6pm</p>
                <p>Saturday: 10am - 4pm</p>
                <p>Sunday: Closed</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className="map-section">
        <h2 className="section-title">Our Location</h2>
        <div className="map-placeholder">
          <p>Map would be displayed here</p>
        </div>
      </section>
    </div>
  );
};

export default Contact;