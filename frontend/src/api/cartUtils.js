import api from '../api/axiosConfig'; // Ensure this path is correct

// Centralized error handler for cart API calls
const handleCartApiError = (error, navigate) => {
  console.error('Cart API error:', error);
  let errorMessage = 'An unexpected error occurred.';

  if (error.response) {
    // The request was made and the server responded with a status code
    // that falls out of the range of 2xx
    if (error.response.status === 401) {
      errorMessage = 'Authentication required. Please log in.';
      // The axios interceptor in axiosConfig.js should already handle logout/redirect,
      // but explicitly navigating here ensures it if the interceptor is bypassed or needs reinforcing.
      // navigate('/login'); // Avoid redundant navigates if interceptor does it.
    } else if (error.response.data && error.response.data.message) {
      errorMessage = error.response.data.message;
    } else {
      errorMessage = `Server error: ${error.response.status} ${error.response.statusText}`;
    }
  } else if (error.request) {
    // The request was made but no response was received
    errorMessage = 'Network error. Please check your internet connection.';
  } else {
    // Something happened in setting up the request that triggered an Error
    errorMessage = error.message;
  }
  throw new Error(errorMessage);
};

// Add item to cart
export const addItemToCartApi = async (productId, quantity, token) => {
  try {
    const response = await api.post(`/users/cart/add`, { productId, quantity });
    return response.data; 
  } catch (error) {
    handleCartApiError(error); 
  }
};

// Fetch user's cart items
export const fetchCartItemsApi = async (token) => {
  try {
    const response = await api.get(`/users/cart`);
    return response.data; 
  } catch (error) {
    handleCartApiError(error); 
  }
};

// Update item quantity in cart
export const updateCartItemQuantityApi = async (productId, quantity, token) => {
  try {
    const response = await api.put(`/users/cart/update`, { productId, quantity });
    return response.data; 
  } catch (error) {
    handleCartApiError(error);
  }
};

// Delete item from cart
export const deleteCartItemApi = async (cartItemId, token) => {
  try {
    const response = await api.delete(`/users/cart/delete/${cartItemId}`);
    return response.data;
  } catch (error) {
    handleCartApiError(error);
  }
};

// Clear the entire cart
export const clearCartApi = async (token) => {
  try {
    const response = await api.delete(`/users/cart/clear`); 
    return response.data;
  } catch (error) {
    handleCartApiError(error);
  }
};