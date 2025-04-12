import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './styles/ProductCard.css';

const ProductCard = ({ product, onCartUpdate }) => {
  const [isAdding, setIsAdding] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);
  const [message, setMessage] = useState('');

  const navigate = useNavigate();

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
      const token = localStorage.getItem('token');
      if (!token) {
        navigate('/login');
        return;
      }

      const response = await fetch('http://localhost:8080/api/cart', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
          productId: product.id,
          quantity: 1
        })
      });

      // Handle all successful responses (2xx)
      if (response.ok) {
        const result = await response.text();
        const message = result
          ? JSON.parse(result)?.message
          : 'Item added to cart';

        setSuccess(true);
        setMessage(message.includes('already') ? 'Already in cart' : 'Added to cart');
        return;
      }

      // Handle special cases
      if (response.status === 409) {
        setSuccess(true); // Treat as success since duplication was prevented
        setMessage('Already in cart');
        return;
      }

      // Handle other errors
      const errorData = await response.json().catch(() => ({}));
      throw new Error(
        errorData.message ||
        `Request failed with status ${response.status}`
      );

    } catch (error) {
      console.error('Add to cart error:', error);
      setError(error.message);

      if (error.message.includes('401')) {
        localStorage.removeItem('token');
        navigate('/login');
      }
    } finally {
      setIsAdding(false);
    }
  };

  const getImageUrl = () => {
    return `http://localhost:8080/uploads/${product.imageName || 'null.png'}`;
  };

  return (
    <div className="product-card">
      <img
        src={getImageUrl()}
        alt={product.name}
        className="product-image"
        onClick={showProductDetails}
        role="button"
        tabIndex={0}
      />
      <div className="product-details">
        <h3 className="product-name">{product.name}</h3>
        <p className="product-price">${product.price}</p>
        <div className="product-footer">
          <span className="product-rating">★ {product.rating}</span>
          <button
          className={`add-to-cart-btn
            ${success ? 'success' : ''}
            ${message?.includes('already') ? 'exists' : ''}`}
          onClick={handleAddToCart}
          disabled={isAdding}
        >
          {isAdding ? 'Adding...' :
          message?.includes('already') ? '✓ In Cart' :
          success ? '✓ Added!' : 'Add to Cart'}
        </button>
          {error && (
            <div className="error-message">
              {error} {error.includes('401') && '- Please login again'}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ProductCard;