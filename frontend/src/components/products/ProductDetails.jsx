import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { fetchProduct } from '../../api/productApi';
import './styles/ProductDetails.css';

const ProductDetails = () => {
  const { id } = useParams();
  const [isAdding, setIsAdding] = useState(false);
  const [success, setSuccess] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();

  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [quantity, setQuantity] = useState(1);

  useEffect(() => {
    const getProduct = async () => {
     try {
        const data = await fetchProduct(id);
        setProduct(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    getProduct();
  }, [id]);

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

  const handleQuantityChange = (e) => {
    const value = parseInt(e.target.value);
    if (value > 0 && value <= product.stockQuantity) {
      setQuantity(value);
    }
  };

  if (loading) return <div className="loading">Loading product details...</div>;
  if (error) return <div className="error">Error: {error}</div>;
  if (!product) return <div className="not-found">Product not found</div>;

 const getImageUrl = () => {
  return `http://localhost:8080/uploads/${product.imageName || 'null.png'}`;
};


  return (
    <div className="product-details-container">
      <button className="back-button" onClick={() => navigate(-1)}>
        &larr; Back to Products
      </button>

      <div className="product-details">
        <div className="product-image-container">
          <img
            src={getImageUrl()}
            alt={product.name }
            className="product-image"
          />
        </div>

        <div className="product-info">
          <h1 className="product-name">{product.name}</h1>
          <p className="product-category">{product.category}</p>

          <div className="product-price">
            ${product.price.toFixed(2)}
            {product.stockQuantity > 0 ? (
              <span className="in-stock">In Stock</span>
            ) : (
              <span className="out-of-stock">Out of Stock</span>
            )}
          </div>

          <div className="product-description">
            <h3>Description</h3>
            <p>{product.description}</p>
          </div>

          {product.stockQuantity > 0 && (
            <div className="product-actions">
              <div className="quantity-selector">
                <label htmlFor="quantity">Quantity:</label>
                <input
                  type="number"
                  id="quantity"
                  min="1"
                  max={product.stockQuantity}
                  value={quantity}
                  onChange={handleQuantityChange}
                />
                <span className="stock-info">({product.stockQuantity} available)</span>
              </div>

              <button
                className="add-to-cart-button"
                onClick={handleAddToCart}
              >
                Add to Cart
              </button>
            </div>
          )}

          <div className="product-meta">
            <p><strong>SKU:</strong> {id}</p>
            <p><strong>Category:</strong> {product.category}</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetails;