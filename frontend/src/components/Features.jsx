import "./Features.css"

function Features() {
  const features = [
    {
      icon: "🛡️",
      title: "Secure Shopping",
      description: "Your data and transactions are protected with industry-leading security measures.",
    },
    {
      icon: "🚚",
      title: "Fast Delivery",
      description: "Quick and reliable shipping to get your electronics to you as soon as possible.",
    },
    {
      icon: "🎧",
      title: "24/7 Support",
      description: "Our expert customer service team is available around the clock to help you.",
    },
    {
      icon: "💳",
      title: "Easy Payments",
      description: "Multiple payment options including credit cards, PayPal, and buy now, pay later.",
    },
  ]

  return (
    <section className="features">
      <div className="features-container">
        <div className="features-header">
          <h2>Why Choose ElectroMart?</h2>
          <p>We're committed to providing you with the best shopping experience for all your electronic needs.</p>
        </div>

        <div className="features-grid">
          {features.map((feature, index) => (
            <div key={index} className="feature-card">
              <div className="feature-icon-wrapper">
                <span className="feature-icon">{feature.icon}</span>
              </div>
              <h3>{feature.title}</h3>
              <p>{feature.description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}

export default Features
