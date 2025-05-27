
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom'; // No need for useLocation
import { fetchProduct } from '../api/productApi';
import { useCart } from '../contexts/CartContext'; 
import './ProductDetailsPage.css'; 

const ProductDetailsPage = () => { // Renamed to ProductDetailsPage for clarity
  const { id } = useParams(); // Get product ID from URL
  const navigate = useNavigate();
  const { addToCart, buyNow } = useCart(); // Get functions from CartContext

  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [quantity, setQuantity] = useState(1);

  useEffect(() => {
    const getProduct = async () => {
      try {
        setLoading(true);
        const data = await fetchProduct(id); // Use the fetchProduct API
        setProduct(data);
      } catch (err) {
        setError(err.message || 'Failed to fetch product details.');
        console.error('Error in ProductDetailsPage:', err);
      } finally {
        setLoading(false);
      }
    };
    getProduct();
  }, [id]); // Re-fetch when ID changes

  const handleAddToCartClick = () => {
    if (!product) return; // Don't add if product data isn't loaded
    addToCart(product, quantity); // Use addToCart from context with selected quantity
  };

  const handleBuyNowClick = () => {
    if (!product) return;
    buyNow(product, quantity); // Use buyNow from context with selected quantity
  };

  const handleQuantityChange = (e) => {
    const value = parseInt(e.target.value, 10); // Parse as integer
    // Ensure quantity is a valid number and within stock limits
    if (!isNaN(value) && value > 0) {
      setQuantity(Math.min(value, product?.stockQuantity || Infinity)); // Cap at stock
    }
  };

  if (loading) return <div className="product-details-container">Loading product details...</div>;
  if (error) return <div className="product-details-container error-message">Error: {error}</div>;
  if (!product) return <div className="product-details-container not-found">Product not found.</div>;

  const getImageUrl = () => {
    return `http://localhost:8080/uploads/${product.imageName || 'default.png'}`;
  };

  return (
    <div className="product-details-container">
      <button className="back-button" onClick={() => navigate(-1)}>
        &larr; Back to Products
      </button>

      <div className="product-details-grid"> {/* Changed class to avoid conflict and better describe layout */}
        <div className="product-image-section">
          <img
            src={getImageUrl()}
            alt={product.name}
            className="product-image-lg"
          />
        </div>

        <div className="product-info-section">
          <h1 className="product-name">{product.name}</h1>
          {product.category && <p className="product-category">Category: {product.category}</p>}

          <div className="price-stock-info">
            <span className="product-price-lg">${product.price?.toFixed(2) || '0.00'}</span>
            {product.stockQuantity > 0 ? (
              <span className="in-stock-label">In Stock ({product.stockQuantity} available)</span>
            ) : (
              <span className="out-of-stock-label">Out of Stock</span>
            )}
          </div>

          <div className="product-description-full">
            <h2>Description</h2>
            <p>{product.description}</p>
          </div>

          {product.stockQuantity > 0 && (
            <div className="product-actions-details">
              <div className="quantity-selector">
                <label htmlFor="quantity">Quantity:</label>
                <input
                  type="number"
                  id="quantity"
                  min="1"
                  max={product.stockQuantity}
                  value={quantity}
                  onChange={handleQuantityChange}
                  className="quantity-input"
                />
              </div>

              <button
                className="add-to-cart-button-lg"
                onClick={handleAddToCartClick}
                disabled={quantity <= 0} // Disable if quantity is invalid
              >
                Add to Cart
              </button>
              <button
                className="buy-now-button-lg"
                onClick={handleBuyNowClick}
                disabled={quantity <= 0} // Disable if quantity is invalid
              >
                Buy Now
              </button>
            </div>
          )}

          <div className="product-meta">
            {product.id && <p><strong>SKU:</strong> {product.id}</p>}
            {/* If product.category is a separate field, use it here, otherwise use the one above */}
            {/* <p><strong>Brand:</strong> {product.brand}</p> */}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetailsPage;