import { useEffect, useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom'; // Correct import for React Router
import './OrdersPage.css'; // Make sure this CSS file exists

function OrdersPage() {
  const { token, isAuthenticated, isLoading, logout } = useAuth();
  const navigate = useNavigate(); // Correct hook for React Router
  const [orders, setOrders] = useState([]);
  const [loadingOrders, setLoadingOrders] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (isLoading) return; // Wait until auth state is loaded

    if (!isAuthenticated()) {
      navigate('/login'); // Correct usage: directly call navigate function
      return;
    }

    const fetchOrders = async () => {
      try {
        setLoadingOrders(true);
        setError(null);

        // This assumes your backend has an endpoint like /api/orders/my
        const response = await fetch("http://localhost:8080/api/orders/my", {
          headers: {
            "Authorization": `Bearer ${token}`, // Include the JWT token
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) {
          if (response.status === 401 || response.status === 403) {
            logout();
            navigate('/login'); // Correct usage
            throw new Error("Session expired or unauthorized. Please log in again.");
          }
          throw new Error(`Failed to fetch orders: ${response.statusText}`);
        }

        const data = await response.json();
        // Assuming data is an array of OrderDto
        setOrders(data);
      } catch (err) {
        setError(err.message);
        console.error("Error fetching orders:", err);
      } finally {
        setLoadingOrders(false);
      }
    };

    if (token) {
      fetchOrders();
    }
  }, [isAuthenticated, isLoading, navigate, token, logout]); // Include navigate in dependencies

  if (isLoading || loadingOrders) {
    return <div className="orders-container">Loading orders...</div>;
  }

  if (error) {
    return <div className="orders-container error-message">Error: {error}</div>;
  }

  if (orders.length === 0) {
    return <div className="orders-container no-orders">You haven't placed any orders yet.</div>;
  }

  return (
    <div className="orders-container">
      <h1>My Orders</h1>
      <div className="order-list">
        {orders.map((order) => (
          <div key={order.id} className="order-card">
            <div className="order-header">
              <h3>Order ID: {order.id}</h3>
              <span className={`order-status ${order.orderStatus.toLowerCase()}`}>{order.orderStatus}</span>
            </div>
            <p><strong>Total Amount:</strong> ${order.totalAmount.toFixed(2)}</p>
            <p><strong>Payment Status:</strong> <span className={`payment-status ${order.paymentStatus.toLowerCase()}`}>{order.paymentStatus}</span></p>
            <p><strong>Delivery Status:</strong> <span className={`delivery-status ${order.deliveryStatus.toLowerCase()}`}>{order.deliveryStatus}</span></p>
            <div className="order-items">
              <h4>Items:</h4>
              <ul>
                {order.orderItems.map((item) => (
                  // Make sure item.id is unique across all order items for keys
                  // If item.product can be null, add a check or default
                  <li key={item.id}>
                    {item.quantity} x {item.productName ? ` " `+ item.productName + ` " ` : 'Product Name Missing'} - ${item.price.toFixed(2)} each
                  </li>
                ))}
              </ul>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default OrdersPage;