import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './styles/ProductCard.css';

const ProductCard = ({ product, updateCartCount }) => {
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
      const token = localStorage.getItem('jwtToken');
      if (!token) {
        navigate('/login');
        return;
      }

      const response = await fetch(
        'http://localhost:8080/api/users/cart/add',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
          body: JSON.stringify({ productId: product.id, quantity: 1 }),
        }
      );

      if (response.ok) {
        setSuccess(true);
        updateCartCount?.(1);
      } else if (response.status === 401) {
        localStorage.removeItem('jwtToken');
        navigate('/login');
      } else {
        const errorData = await response.json().catch(() => null);
        throw new Error(errorData?.message || response.statusText);
      }
    } catch (error) {
      console.error('Add to cart error:', error);
      setError(error.message || 'Failed to add to cart');
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