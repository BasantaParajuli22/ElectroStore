import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './styles/ProductCard.css';

const ProductCard = ({ product, user, updateCartCount }) => {
  const [isAdding, setIsAdding] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  const showProductDetails = () => {
    navigate(`/products/${product.id}`);
  };

  const handleAddToCart = async (e) => {
    e.stopPropagation();
    if (isAdding) return;

    setIsAdding(true);
    setError(null);
    setSuccess(false);

    try {
      const user = JSON.parse(localStorage.getItem('user'));
      if (!user?.email) {
        navigate('/login');
        return;
      }

      const response = await fetch(
        `http://localhost:8080/api/users/cart/add?email=${user.email}`,
        {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ productId: product.id, quantity: 1 }),
        }
      );

      // Check if the request was successful (2xx status)
      if (response.ok) {
        setSuccess(true);
        updateCartCount?.(1); // Optional chaining in case prop isn't passed
      } else {
        // Try to parse error message, fallback to status text
        const errorData = await response.json().catch(() => null);
        throw new Error(errorData?.message || response.statusText);
      }
    } catch (error) {
      console.error('Add to cart error:', error);
      setError(error.message || 'Failed to add to cart');

      // Handle unauthorized (401) specifically
      if (error.message.includes('401')) {
        localStorage.removeItem('user');
        navigate('/login');
      }
    } finally {
      setIsAdding(false);
    }
  };

  return (
    <div className="product-card" onClick={showProductDetails}>
      <img
        src={`http://localhost:8080/uploads/${product.imageName || 'default.png'}`}
        alt={product.name}
        className="product-image"
      />
      <div className="product-details">
        <h3 className="product-name">{product.name}</h3>
        <p className="product-price">${product.price.toFixed(2)}</p>
        <div className="product-footer">
          <span className="product-rating">★ {product.rating?.toFixed(1) || '0.0'}</span>
          <button
            className={`add-to-cart-btn ${success ? 'success' : ''}`}
            onClick={handleAddToCart}
            disabled={isAdding}
          >
            {isAdding ? 'Adding...' : success ? '✓ Added!' : 'Add to Cart'}
          </button>
          {error && <div className="error-message">{error}</div>}
        </div>
      </div>
    </div>
  );
};

export default ProductCard;