import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from './AuthContext';

import {
  addItemToCartApi,
  fetchCartItemsApi,
  updateCartItemQuantityApi,
  deleteCartItemApi,
  clearCartApi
} from '../api/cartUtils';

const CartContext = createContext();

export const useCart = () => {
  const context = useContext(CartContext);
  if (context === undefined) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
};

export const CartProvider = ({ children }) => {
  const [cartItems, setCartItems] = useState([]);
  const [cartLoading, setCartLoading] = useState(true);
  const [cartError, setCartError] = useState(null);

  const navigate = useNavigate();
  const { isAuthenticated, token, isLoading: authLoading, user, logout } = useAuth();

  const fetchCart = useCallback(async () => {
    if (authLoading || !isAuthenticated()) {
      setCartItems([]);
      setCartLoading(false);
      return;
    }

    setCartLoading(true);
    setCartError(null);
    try {
      const data = await fetchCartItemsApi(token);
      setCartItems(data);
    } catch (err) {
      console.error('Error fetching cart:', err);
      setCartError(err.message || 'Failed to load cart.');
      if (err.message.includes('401') || err.message.includes('Unauthorized')) {
          logout();
          navigate('/login');
      }
      setCartItems([]);
    } finally {
      setCartLoading(false);
    }
  }, [authLoading, isAuthenticated, token, logout, navigate]);

  useEffect(() => {
    fetchCart();
  }, [fetchCart]);

  const cartCount = cartItems.reduce((total, item) => total + item.quantity, 0);
  const cartTotal = cartItems.reduce((total, item) => total + (item.product?.price * item.quantity), 0);

  const addToCart = async (product, quantity = 1) => {
    if (!isAuthenticated()) {
      alert("Please log in to add items to your cart.");
      navigate('/login');
      return { success: false, error: "Not authenticated" };
    }

    setCartLoading(true);
    try {
      await addItemToCartApi(product.id, quantity, token); // No need to capture response directly here
      await fetchCart(); // Re-fetch the entire cart to ensure state is up-to-date
      alert(`${product.name} added to cart!`);
      return { success: true, message: 'Product added to cart!' };
    } catch (err) {
      console.error('Error adding to cart:', err);
      alert(`Error adding ${product.name} to cart: ${err.message}`);
      return { success: false, error: err.message };
    } finally {
      setCartLoading(false);
    }
  };

  const updateCartQuantity = async (cartItemId, newQuantity) => {
    if (!isAuthenticated()) {
      alert("Please log in to update your cart.");
      navigate('/login');
      return;
    }
    if (newQuantity < 1) {
      await removeCartItem(cartItemId);
      return;
    }

    setCartLoading(true);
    try {
      const itemToUpdate = cartItems.find(item => item.cartItemId === cartItemId);
      if (!itemToUpdate) {
        throw new Error("Cart item not found.");
      }
      const productId = itemToUpdate.product.id;

      // The backend updateCartItemQuantityApi returns the updated cart, so we can use it directly
      const updatedCart = await updateCartItemQuantityApi(productId, newQuantity, token);
      setCartItems(updatedCart);
      alert(`Quantity for ${itemToUpdate.product.name} updated to ${newQuantity}.`);
    } catch (err) {
      console.error('Error updating cart quantity:', err);
      alert(`Error updating quantity: ${err.message}`);
    } finally {
      setCartLoading(false);
    }
  };

  const removeCartItem = async (cartItemId) => {
    if (!isAuthenticated()) {
      alert("Please log in to modify your cart.");
      navigate('/login');
      return;
    }

    setCartLoading(true);
    try {
      const itemToRemove = cartItems.find(item => item.cartItemId === cartItemId);
      await deleteCartItemApi(cartItemId, token);
      // After deletion, re-fetch the cart to ensure consistency
      await fetchCart();
      alert(`${itemToRemove?.product?.name || 'Item'} removed from cart.`);
    } catch (err) {
      console.error('Error removing item from cart:', err);
      alert(`Error removing item: ${err.message}`);
    } finally {
      setCartLoading(false);
    }
  };

  const clearCart = async () => {
    if (!isAuthenticated()) {
      alert("Please log in to clear your cart.");
      navigate('/login');
      return;
    }

    setCartLoading(true);
    try {
      await clearCartApi(token);
      setCartItems([]);
      alert("Your cart has been cleared!");
    } catch (err) {
      console.error('Error clearing cart:', err);
      alert(`Error clearing cart: ${err.message}`);
    } finally {
      setCartLoading(false);
    }
  };

  const buyNow = (product, quantity = 1) => {
    if (!isAuthenticated()) {
      alert("Please log in to proceed with your purchase.");
      navigate('/login');
      return;
    }

    console.log(`Initiating buy now for ${quantity} of ${product.name} (ID: ${product.id})`);
    alert(`Proceeding to checkout for ${product.name}!`);
  };

  const value = {
    cartItems,
    cartCount,
    cartTotal,
    cartLoading,
    cartError,
    addToCart,
    updateCartQuantity,
    removeCartItem,
    clearCart,
    buyNow,
    fetchCart
  };

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
};