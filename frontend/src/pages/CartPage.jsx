// src/pages/CartPage.jsx
import React, { useEffect } from 'react'; // Removed useState, using context state
import { useCart } from '../contexts/CartContext'; // Import useCart hook
import './Cart.css';

const CartPage = () => {
  const {
    cartItems,
    cartLoading,
    cartError,
    updateCartQuantity,
    removeCartItem,
    clearCart,
    fetchCart, // We can expose this if we want a manual refresh button
    cartTotal
  } = useCart(); // Destructure everything from useCart

  useEffect(() => {
    // You can choose to refresh cart explicitly here,
    // or rely on CartContext's internal useEffect to fetch on auth change.
    // fetchCart(); // Uncomment if you want to force a refetch on page load
  }, [fetchCart]); // Dependency on fetchCart callback from context

  if (cartLoading) {
    return <div className="cart-page loading-state">Loading your cart...</div>;
  }

  if (cartError) {
    return <div className="cart-page error-message">Error: {cartError}</div>;
  }

  if (cartItems.length === 0) {
    return <div className="cart-page empty-cart">Your cart is empty.</div>;
  }

  return (
    <div className="cart-page">
      <h2>Your Cart</h2>
      <ul className="cart-items">
        {cartItems.map(item => (
          <li key={item.cartItemId} className="cart-item">
            <div className="item-details">
              <img
                src={`http://localhost:8080/uploads/${item.product?.imageName || 'default.png'}`}
                alt={item.product?.name}
                className="item-image"
              />
              <div className="item-info">
                <h3 className="item-name">{item.product?.name}</h3>
                <p className="item-price">${item.product?.price?.toFixed(2)}</p>
              </div>
            </div>
            <div className="item-quantity-controls">
              <button
                onClick={() => updateCartQuantity(item.cartItemId, item.quantity - 1)}
                disabled={item.quantity <= 1} // Disable decrement if quantity is 1
              >
                -
              </button>
              <span>{item.quantity}</span>
              <button
                onClick={() => updateCartQuantity(item.cartItemId, item.quantity + 1)}
                disabled={item.quantity >= (item.product?.stockQuantity || Infinity)} // Disable if at max stock
              >
                +
              </button>
            </div>
            <button className="remove-item-btn" onClick={() => removeCartItem(item.cartItemId)}>
              Remove
            </button>
          </li>
        ))}
      </ul>
      <div className="cart-summary">
        <div className="cart-total">
          Total: ${cartTotal.toFixed(2)}
        </div>
        <div className="cart-actions">
          <button className="clear-cart-btn" onClick={clearCart}>Clear Cart</button>
          <button className="checkout-btn">Proceed to Checkout</button>
        </div>
      </div>
    </div>
  );
};

export default CartPage;