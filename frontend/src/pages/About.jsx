import "./About.css"

function About() {
  return (
    <div className="about-container">
      <div className="about-content">
        <h1 className="about-title">About ElectroMart</h1>

        <div className="about-cards">
          <div className="about-card">
            <h3>Our Mission</h3>
            <p>
              At ElectroMart, we're dedicated to bringing you the latest and greatest in electronics. From cutting-edge
              smartphones to innovative home appliances, we curate the best products to enhance your digital lifestyle.
            </p>
          </div>

          <div className="about-card">
            <h3>Our Vision</h3>
            <p>
              To be the leading destination for electronics enthusiasts, providing exceptional products, competitive
              prices, and outstanding customer service that exceeds expectations.
            </p>
          </div>
        </div>

        <div className="why-choose">
          <h2>Why Choose ElectroMart?</h2>
          <div className="features-grid">
            <div className="feature-item">
              <h3>Quality Products</h3>
              <p>We partner with trusted brands to ensure every product meets our high standards.</p>
            </div>
            <div className="feature-item">
              <h3>Fast Shipping</h3>
              <p>Quick and reliable delivery to get your electronics to you as soon as possible.</p>
            </div>
            <div className="feature-item">
              <h3>Expert Support</h3>
              <p>Our knowledgeable team is here to help you find the perfect electronics for your needs.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default About
