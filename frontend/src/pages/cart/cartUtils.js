// src/utils/cartUtils.js
export const handleAddToCart = async (productId, updateCartCount, navigate) => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (!user?.email) {
      navigate('/login');
      return { error: 'User not logged in' };
    }

    try {
      const response = await fetch(
        `http://localhost:8080/api/users/cart/add`,
        {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ productId, quantity: 1 }),
        }
      );

      if (response.ok) {
        updateCartCount?.(1);
        return { success: true };
      } else {
        const errorData = await response.json().catch(() => null);
        throw new Error(errorData?.message || response.statusText);
      }
    } catch (error) {
      console.error('Add to cart error:', error);
      if (error.message.includes('401')) {
        localStorage.removeItem('user');
        navigate('/login');
      }
      return { error: error.message || 'Failed to add to cart' };
    }
  };