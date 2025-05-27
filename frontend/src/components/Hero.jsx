import { useState } from "react"
import { Link } from "react-router-dom"
import AuthModal from "./AuthModal"
import "./Hero.css"

function Hero() {
  const [isAuthModalOpen, setIsAuthModalOpen] = useState(false)

  const openRegisterModal = () => {
    setIsAuthModalOpen(true)
  }

  return (
    <section className="hero">
      <div className="hero-container">
        <div className="hero-content">
          <h1 className="hero-title">
            Welcome to <span className="hero-brand">ElectroMart</span>
          </h1>
          <p className="hero-description">
            Discover the latest in electronics and technology. From smartphones to smart homes, we bring you
            cutting-edge products at unbeatable prices.
          </p>
          <div className="hero-buttons">
            <button onClick={openRegisterModal} className="hero-btn-primary">
              Get Started →
            </button>
            <Link to="/about">
              <button className="hero-btn-secondary">Learn More</button>
            </Link>
          </div>

          {/* Feature Icons */}
          <div className="hero-features">
            <div className="feature-card">
              <div className="feature-icon">📱</div>
              <h3>Latest Smartphones</h3>
              <p>Cutting-edge mobile technology from top brands</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">💻</div>
              <h3>Powerful Laptops</h3>
              <p>High-performance computers for work and gaming</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">🎧</div>
              <h3>Audio Gear</h3>
              <p>Premium headphones and speakers for audiophiles</p>
            </div>
          </div>
        </div>
      </div>
      <AuthModal isOpen={isAuthModalOpen} onClose={() => setIsAuthModalOpen(false)} initialMode="register" />
    </section>
  )
}

export default Hero
