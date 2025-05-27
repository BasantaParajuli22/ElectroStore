// import React, { useState, useEffect } from 'react';
// import api from '../../api/axiosConfig'; // Import the configured axios instance
// import './styles/Cart.css'; // Your cart-specific CSS

// const CartPage = ({ updateCartCount }) => {
//   const [cartItems, setCartItems] = useState([]);
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState(null);

//   useEffect(() => {
//     fetchCartData();
//   }, []); // Empty dependency array means this runs once on mount

//   const fetchCartData = async () => {
//     setLoading(true);
//     setError(null);
//     try {
//       // Use the 'api' instance, it will automatically attach the JWT token
//       const response = await api.get('/users/cart');
//       setCartItems(response.data);
//     } catch (err) {
//       // The interceptor in axiosConfig.js will handle 401 errors globally
//       // So, here we just handle other errors or messages
//       setError(err.response?.data?.message || 'Failed to fetch cart items.');
//       console.error('Error fetching cart:', err);
//     } finally {
//       setLoading(false);
//     }
//   };

//   const handleDeleteItem = async (cartItemId) => {
//     try {
//       // Use the 'api' instance
//       await api.delete(`/users/cart/delete/${cartItemId}`);
//       setCartItems(prevItems => prevItems.filter(item => item.cartItemId !== cartItemId));
//       updateCartCount(-1); // Decrement cart count in App.js
//     } catch (err) {
//       setError(err.response?.data?.message || 'Failed to delete item.');
//       console.error('Error deleting item:', err);
//     }
//   };

//   const handleUpdateQuantity = async (cartItemId, quantity) => {
//     if (quantity < 1) { // Prevent negative or zero quantity
//       handleDeleteItem(cartItemId); // Consider deleting if quantity goes to 0
//       return;
//     }

//     try {
//       // Use the 'api' instance
//       const response = await api.put('/users/cart/update', {
//         // Find the product ID from the current cart items state
//         productId: cartItems.find(item => item.cartItemId === cartItemId)?.product?.id,
//         quantity
//       });
//       if (response.status === 200) { // Assuming 200 OK for successful update
//         setCartItems(prevItems =>
//           prevItems.map(item =>
//             item.cartItemId === cartItemId ? { ...item, quantity } : item
//           )
//         );
//         // If your backend response includes the new total cart count, you can update it here
//         // For simplicity, we just update the individual item quantity.
//       }
//     } catch (err) {
//       setError(err.response?.data?.message || 'Failed to update quantity.');
//       console.error('Error updating quantity:', err);
//     }
//   };

//   if (loading) {
//     return <div className="text-center p-4">Loading your cart...</div>;
//   }

//   if (error) {
//     return <div className="text-center p-4 text-red-600">Error: {error}</div>;
//   }

//   if (cartItems.length === 0) {
//     return <div className="text-center p-4 text-gray-600">Your cart is empty.</div>;
//   }

//   return (
//     <div className="cart-page p-4">
//       <h2 className="text-3xl font-bold mb-6 text-center">Your Cart</h2>
//       <ul className="cart-items divide-y divide-gray-200">
//         {cartItems.map(item => (
//           <li key={item.cartItemId} className="cart-item flex items-center justify-between py-4">
//             <div className="flex items-center space-x-4">
//               <img
//                 src={`http://localhost:8080/uploads/${item.product?.imageName || 'default.png'}`}
//                 alt={item.product?.name}
//                 className="w-20 h-20 object-cover rounded-lg shadow-md"
//               />
//               <div className="item-info">
//                 <h3 className="item-name text-lg font-semibold">{item.product?.name}</h3>
//                 <p className="item-price text-gray-700">${item.product?.price?.toFixed(2)}</p>
//               </div>
//             </div>
//             <div className="item-quantity flex items-center space-x-2">
//               <button
//                 onClick={() => handleUpdateQuantity(item.cartItemId, item.quantity - 1)}
//                 className="bg-gray-200 text-gray-700 w-8 h-8 rounded-full flex items-center justify-center hover:bg-gray-300"
//               >-</button>
//               <span className="text-lg font-medium">{item.quantity}</span>
//               <button
//                 onClick={() => handleUpdateQuantity(item.cartItemId, item.quantity + 1)}
//                 className="bg-gray-200 text-gray-700 w-8 h-8 rounded-full flex items-center justify-center hover:bg-gray-300"
//               >+</button>
//             </div>
//             <button
//               className="remove-item bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded-lg transition duration-200"
//               onClick={() => handleDeleteItem(item.cartItemId)}
//             >
//               Remove
//             </button>
//           </li>
//         ))}
//       </ul>
//       <div className="cart-total text-right text-2xl font-bold mt-8 p-4 bg-gray-100 rounded-lg shadow-inner">
//         Total: ${cartItems.reduce((total, item) => total + (item.product?.price * item.quantity), 0).toFixed(2)}
//       </div>
//       <div className="text-center mt-6">
//         <button className="bg-green-600 hover:bg-green-700 text-white font-bold py-3 px-8 rounded-lg text-xl shadow-lg transition duration-200">
//           Proceed to Checkout
//         </button>
//       </div>
//     </div>
//   );
// };

// export default CartPage;