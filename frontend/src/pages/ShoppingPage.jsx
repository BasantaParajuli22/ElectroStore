// src/pages/ShoppingPage.jsx
import React, { useEffect, useState } from 'react';
import { fetchProducts } from '../api/productApi'; 
import ProductCard from '../components/products/ProductCard'; 
import { useCart } from '../contexts/CartContext'; 
import './ShoppingPage.css'; 

function ShoppingPage() { // Renamed from Products to ShoppingPage
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const getProducts = async () => {
      try {
        setLoading(true); // Set loading true at the start of fetch
        const data = await fetchProducts();
        setProducts(data);
      } catch (err) {
        setError(err.message || 'Failed to fetch products.'); // More robust error message
        console.error('Error in ShoppingPage:', err);
      } finally {
        setLoading(false);
      }
    };

    getProducts();
  }, []); // Empty dependency array means this runs once on mount

  if (loading) {
    return <div className="shopping-container">Loading products...</div>;
  }

  if (error) {
    return <div className="shopping-container error-message">Error: {error}</div>;
  }

  if (products.length === 0) {
    return <div className="shopping-container no-products">No products available.</div>;
  }

  return (
    <div className="shopping-container">
      <h1>Our Products</h1>
      <div className="product-list"> {/* Renamed from products-grid to product-list for consistency with prev example */}
        {products.map(product => (
          // ProductCard itself will handle navigation and add-to-cart using contexts
          <ProductCard key={product.id} product={product} />
        ))}
      </div>
    </div>
  );
}

export default ShoppingPage;