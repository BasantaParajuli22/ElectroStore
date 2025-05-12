import React, { useState, useEffect } from 'react';
import './styles/Cart.css';

const CartPage = ({ updateCartCount }) => {
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchCartData();
  }, []);

  const fetchCartData = async () => {
    setLoading(true);
    setError(null);
    try {
      const token = localStorage.getItem('jwtToken');
      if (!token) {
        setError('Unauthorized');
        setLoading(false);
        return;
      }
      const response = await fetch('http://localhost:8080/api/users/cart', {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      if (response.ok) {
        const data = await response.json();
        setCartItems(data);
      } else if (response.status === 401) {
        setError('Unauthorized');
        localStorage.removeItem('jwtToken');
        // Optionally redirect to login
      } else {
        setError('Failed to fetch cart items');
      }
    } catch (error) {
      setError('Failed to fetch cart items');
      console.error('Error fetching cart:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteItem = async (cartItemId) => {
    try {
      const token = localStorage.getItem('jwtToken');
      if (!token) {
        setError('Unauthorized');
        return;
      }
      const response = await fetch(`http://localhost:8080/api/users/cart/delete/${cartItemId}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      if (response.ok) {
        setCartItems(prevItems => prevItems.filter(item => item.cartItemId !== cartItemId));
        updateCartCount(-1);
      } else if (response.status === 404) {
        setError('Item not found in cart.');
      } else if (response.status === 401) {
        setError('Unauthorized');
        localStorage.removeItem('jwtToken');
        // Optionally redirect to login
      } else {
        setError('Failed to delete item.');
      }
    } catch (error) {
      setError('Failed to delete item.');
      console.error('Error deleting item:', error);
    }
  };

  const handleUpdateQuantity = async (cartItemId, quantity) => {
    if (quantity < 1) return; // Prevent negative or zero quantity

    try {
      const token = localStorage.getItem('jwtToken');
      if (!token) {
        setError('Unauthorized');
        return;
      }
      const response = await fetch('http://localhost:8080/api/users/cart/update', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({ productId: cartItems.find(item => item.cartItemId === cartItemId)?.product?.id, quantity }),
      });
      if (response.ok) {
        setCartItems(prevItems =>
          prevItems.map(item =>
            item.cartItemId === cartItemId ? { ...item, quantity } : item
          )
        );
        // Optionally update cart count if needed (depends on backend response)
      } else if (response.status === 400) {
        const errorData = await response.json().catch(() => null);
        setError(errorData?.error || 'Failed to update quantity.');
      } else if (response.status === 401) {
        setError('Unauthorized');
        localStorage.removeItem('jwtToken');
        // Optionally redirect to login
      } else {
        setError('Failed to update quantity.');
      }
    } catch (error) {
      setError('Failed to update quantity.');
      console.error('Error updating quantity:', error);
    }
  };

  if (loading) {
    return <div>Loading your cart...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (cartItems.length === 0) {
    return <div>Your cart is empty.</div>;
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
            <div className="item-quantity">
              <button onClick={() => handleUpdateQuantity(item.cartItemId, item.quantity - 1)}>-</button>
              <span>{item.quantity}</span>
              <button onClick={() => handleUpdateQuantity(item.cartItemId, item.quantity + 1)}>+</button>
            </div>
            <button className="remove-item" onClick={() => handleDeleteItem(item.cartItemId)}>
              Remove
            </button>
          </li>
        ))}
      </ul>
      <div className="cart-total">
        Total: ${cartItems.reduce((total, item) => total + (item.product?.price * item.quantity), 0).toFixed(2)}
      </div>
      {/* Add checkout button or other cart actions here */}
    </div>
  );
};

export default CartPage;