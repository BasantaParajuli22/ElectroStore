import React from 'react';

import './styles/Home.css';
import ProductCard from '../../components/products/ProductCard';
import { useNavigate } from 'react-router-dom';

const Home = () => {
  const navigate = useNavigate();

  const featuredProducts = [
    {
      id: 1,
      name: 'Ultra Thin Laptop',
      price: 899.99,
      rating: 4.5,
      image: 'https://images.unsplash.com/photo-1499914485622-a88fac536970?w=400&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8N3x8bGFwdG9wfGVufDB8fDB8fHww'
    },
    {
      id: 2,
      name: 'Gaming PC',
      price: 1499.99,
      rating: 4.8,
      image: 'https://images.unsplash.com/photo-1525547719571-a2d4ac8945e2?w=400&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8bGFwdG9wfGVufDB8fDB8fHww'
    },
    {
      id: 3,
      name: 'Wireless Headphones',
      price: 199.99,
      rating: 4.2,
      image: 'https://media.istockphoto.com/id/860853774/photo/blue-headphones-isolated-on-a-white-background.jpg?s=612x612&w=0&k=20&c=KqMSLWuM_Prrq5XHTe79bnFRU_leFDaXTuKqup5uvrE='
    },
    {
      id: 4,
      name: 'Smartphone Pro',
      price: 799.99,
      rating: 4.7,
      image: 'https://imgs.search.brave.com/qWN-7xXdN3_wK2jaL9V_8C-IRit59GPSmZ_fQzsP8mM/rs:fit:500:0:0:0/g:ce/aHR0cHM6Ly9zdG9y/YWdlLmdvb2dsZWFw/aXMuY29tL2d3ZWIt/dW5pYmxvZy1wdWJs/aXNoLXByb2QvaW1h/Z2VzLzVfV2F5c19Q/aXhlbF9TdHVkaW8u/bWF4LTEyMDB4NDE2/LmZvcm1hdC13ZWJw/LndlYnA'
    },
    {
      id: 5,
      name: '4K Monitor',
      price: 499.99,
      rating: 4.6,
      image: 'https://i.pinimg.com/736x/5b/cc/69/5bcc69df4dfd18287eef45d086b44159.jpg'
    },
    {
      id: 6,
      name: 'Mechanical Keyboard',
      price: 129.99,
      rating: 4.4,
      image: 'https://i.pinimg.com/236x/f8/e4/df/f8e4dfa9f2fbbf8de1c169ee3f9ee0a5.jpg'
    }
  ];

  const navigateToShop = ()  =>{
    navigate('/shop');
  }

  return (
    <div className="home-container">
      <section className="hero-section">
        <div className="hero-content">
          <h1 className="hero-title">Welcome to ElectroStore</h1>
          <p className="hero-subtitle">Discover the latest electronics at unbeatable prices</p>
          <button className="hero-button" onClick={navigateToShop}>
            Shop Now
          </button>
        </div>
      </section>

      <section className="products-section">
        <h2 className="section-title">Featured Products</h2>
        <div className="products-grid">
          {featuredProducts.map(product => (
            <ProductCard key={product.id} product={product} />
          ))}
        </div>
      </section>

      <section className="features-section">
        <h2 className="section-title">Why Choose ElectroStore?</h2>
        <div className="features-grid">
          <div className="feature-card">
            <h3 className="feature-title">Free Shipping</h3>
            <p>On all orders over $50</p>
          </div>
          <div className="feature-card">
            <h3 className="feature-title">30-Day Returns</h3>
            <p>No questions asked</p>
          </div>
          <div className="feature-card">
            <h3 className="feature-title">24/7 Support</h3>
            <p>Dedicated support</p>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;