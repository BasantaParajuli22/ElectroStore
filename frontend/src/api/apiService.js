const API_BASE_URL = 'http://localhost:8080/api';

export async function fetchProducts() {
  const response = await fetch(`${API_BASE_URL}/products`);
  if (!response.ok) {
    throw new Error('Failed to fetch products');
  }
  return await response.json();
}

export async function addToCart(productId, quantity = 1) {
  const response = await fetch(`${API_BASE_URL}/cart`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ productId, quantity }),
  });
  if (!response.ok) {
    throw new Error('Failed to add to cart');
  }
  return await response.json();
}

export async function getCart() {
  const response = await fetch(`${API_BASE_URL}/cart`);
  if (!response.ok) {
    throw new Error('Failed to fetch cart');
  }
  return await response.json();
}

export async function updateCartItem(productId, quantity) {
  const response = await fetch(`${API_BASE_URL}/cart/${productId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ quantity }),
  });
  if (!response.ok) {
    throw new Error('Failed to update cart item');
  }
  return await response.json();
}

export async function removeFromCart(productId) {
  const response = await fetch(`${API_BASE_URL}/cart/${productId}`, {
    method: 'DELETE',
  });
  if (!response.ok) {
    throw new Error('Failed to remove from cart');
  }
  return await response.json();
}