import "./Contact.css"

function Contact() {
  return (
    <div className="contact-container">
      <div className="contact-content">
        <h1 className="contact-title">Contact Us</h1>
        <p className="contact-subtitle">
          Have questions about our products or need assistance? We're here to help! Reach out to us through any of the
          methods below.
        </p>

        <div className="contact-grid">
          {/* Contact Form */}
          <div className="contact-form-card">
            <h3>Send us a Message</h3>
            <p className="form-description">Fill out the form below and we'll get back to you as soon as possible.</p>

            <form className="contact-form">
              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="firstName">First Name</label>
                  <input id="firstName" type="text" placeholder="John" />
                </div>
                <div className="form-group">
                  <label htmlFor="lastName">Last Name</label>
                  <input id="lastName" type="text" placeholder="Doe" />
                </div>
              </div>
              <div className="form-group">
                <label htmlFor="email">Email</label>
                <input id="email" type="email" placeholder="john@example.com" />
              </div>
              <div className="form-group">
                <label htmlFor="subject">Subject</label>
                <input id="subject" type="text" placeholder="How can we help you?" />
              </div>
              <div className="form-group">
                <label htmlFor="message">Message</label>
                <textarea id="message" placeholder="Tell us more about your inquiry..." rows="5"></textarea>
              </div>
              <button type="submit" className="submit-btn">
                Send Message
              </button>
            </form>
          </div>

          {/* Contact Information */}
          <div className="contact-info">
            <div className="info-card">
              <h4>📍 Our Location</h4>
              <p>
                123 Electronics Street
                <br />
                Tech District, TD 12345
                <br />
                United States
              </p>
            </div>

            <div className="info-card">
              <h4>📞 Phone</h4>
              <p>
                Customer Service: (555) 123-4567
                <br />
                Technical Support: (555) 123-4568
                <br />
                Business Hours: Mon-Fri 9AM-6PM EST
              </p>
            </div>

            <div className="info-card">
              <h4>✉️ Email</h4>
              <p>
                General Inquiries: info@electromart.com
                <br />
                Support: support@electromart.com
                <br />
                Sales: sales@electromart.com
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Contact
