import "./Newsletter.css"

function Newsletter() {
  return (
    <section className="newsletter">
      <div className="newsletter-container">
        <div className="newsletter-content">
          <h2>Stay Updated</h2>
          <p>
            Subscribe to our newsletter and be the first to know about new products, exclusive deals, and tech news.
          </p>
          <div className="newsletter-form">
            <input type="email" placeholder="Enter your email" className="newsletter-input" />
            <button className="newsletter-btn">Subscribe</button>
          </div>
          <p className="newsletter-disclaimer">We respect your privacy. Unsubscribe at any time.</p>
        </div>
      </div>
    </section>
  )
}

export default Newsletter
