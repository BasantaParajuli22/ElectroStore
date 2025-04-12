import React, { useState } from 'react';
import './auth.css';

const AuthSystem = () => {
  const [activeTab, setActiveTab] = useState('login');
  const [token, setToken] = useState(localStorage.getItem('token') || '');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  // Login state
  const [loginData, setLoginData] = useState({
    email: '',
    password: ''
  });

  // Registration states
  const [basicUserData, setBasicUserData] = useState({
    name: '',
    email: '',
    password: '',
    role: 'CUSTOMER'
  });

  const [customerData, setCustomerData] = useState({
    address: '',
    phone: ''
  });

  const [fullRegistrationData, setFullRegistrationData] = useState({
    userDto: {
      name: '',
      email: '',
      password: '',
      role: 'CUSTOMER'
    },
    customerDto: {
      address: '',
      phone: ''
    }
  });

  // Handle login input changes
  const handleLoginChange = (e) => {
    const { name, value } = e.target;
    setLoginData(prev => ({ ...prev, [name]: value }));
    setError(''); // Clear error when user types
  };

  // Handle basic user registration changes
  const handleBasicUserChange = (e) => {
    const { name, value } = e.target;
    setBasicUserData(prev => ({ ...prev, [name]: value }));
    setError('');
  };

  // Handle customer details changes
  const handleCustomerChange = (e) => {
    const { name, value } = e.target;
    setCustomerData(prev => ({ ...prev, [name]: value }));
    setError('');
  };

  // Handle full registration changes
  const handleFullRegistrationChange = (section, e) => {
    const { name, value } = e.target;
    setFullRegistrationData(prev => ({
      ...prev,
      [section]: {
        ...prev[section],
        [name]: value
      }
    }));
    setError('');
  };

  // Login function
  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          email: loginData.email,
          password: loginData.password
        })
      });

      // First check if response is OK
      if (!response.ok) {
        // Try to parse error response as JSON
        let errorMessage = 'Login failed';
        try {
          const errorData = await response.text(); // Use text() instead of json()
          errorMessage = errorData || 'Login failed';
        } catch (parseError) {
          console.error('Error parsing error response:', parseError);
        }
        throw new Error(errorMessage);
      }

      // If response is OK, parse as JSON
      const data = await response.json();
      setToken(data.jwt);
      localStorage.setItem('token', data.jwt);
    } catch (error) {
      console.error('Login error:', error);
      setError(error.message || 'Login failed. Please check your credentials.');
    } finally {
      setLoading(false);
    }
  };

  // Basic user registration
  const handleBasicSignup = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await fetch('http://localhost:8080/signup', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(basicUserData)
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Registration failed');
      }

      alert('User created successfully!');
      setActiveTab('login');
    } catch (error) {
      console.error('Registration error:', error);
      setError(error.message || 'Registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  // Full registration (user + customer)
  const handleFullRegistration = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await fetch('http://localhost:8080/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(fullRegistrationData)
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Registration failed');
      }

      alert('User and customer registered successfully!');
      setActiveTab('login');
    } catch (error) {
      console.error('Full registration error:', error);
      setError(error.message || 'Registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  // Logout function
  const handleLogout = () => {
    setToken('');
    localStorage.removeItem('token');
  };

  return (
    <div className="auth-container">
      <div className="auth-header">
        <h1>Authentication System</h1>
        {token && (
          <button onClick={handleLogout} className="logout-btn">
            Logout
          </button>
        )}
      </div>

      {!token ? (
        <div className="auth-tabs">
          <div className="tab-buttons">
            <button
              className={activeTab === 'login' ? 'active' : ''}
              onClick={() => setActiveTab('login')}
            >
              Login
            </button>
            <button
              className={activeTab === 'signup' ? 'active' : ''}
              onClick={() => setActiveTab('signup')}
            >
              Basic Signup
            </button>
            <button
              className={activeTab === 'details' ? 'active' : ''}
              onClick={() => setActiveTab('details')}
            >
              Add Customer Details
            </button>
            <button
              className={activeTab === 'full' ? 'active' : ''}
              onClick={() => setActiveTab('full')}
            >
              Full Registration
            </button>
          </div>

          <div className="tab-content">
            {error && <div className="error-message">{error}</div>}

            {activeTab === 'login' && (
              <form onSubmit={handleLogin} className="auth-form">
                <h2>Login</h2>
                <div className="form-group">
                  <label>Email:</label>
                  <input
                    type="email"
                    name="email"
                    value={loginData.email}
                    onChange={handleLoginChange}
                    autoComplete="username"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Password:</label>
                  <input
                    type="password"
                    name="password"
                    value={loginData.password}
                    onChange={handleLoginChange}
                    autoComplete="current-password"
                    required
                  />
                </div>
                <button type="submit" disabled={loading}>
                  {loading ? 'Logging in...' : 'Login'}
                </button>
              </form>
            )}

            {activeTab === 'signup' && (
              <form onSubmit={handleBasicSignup} className="auth-form">
                <h2>Basic User Registration</h2>
                <div className="form-group">
                  <label>Name:</label>
                  <input
                    type="text"
                    name="name"
                    value={basicUserData.name}
                    onChange={handleBasicUserChange}
                    autoComplete="name"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Email:</label>
                  <input
                    type="email"
                    name="email"
                    value={basicUserData.email}
                    onChange={handleBasicUserChange}
                    autoComplete="email"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Password:</label>
                  <input
                    type="password"
                    name="password"
                    value={basicUserData.password}
                    onChange={handleBasicUserChange}
                    autoComplete="new-password"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Role:</label>
                  <select
                    name="role"
                    value={basicUserData.role}
                    onChange={handleBasicUserChange}
                  >
                    <option value="CUSTOMER">Customer</option>
                    <option value="ADMIN">Admin</option>
                  </select>
                </div>
                <button type="submit" disabled={loading}>
                  {loading ? 'Registering...' : 'Register'}
                </button>
              </form>
            )}

            {activeTab === 'full' && (
              <form onSubmit={handleFullRegistration} className="auth-form">
                <h2>Full Registration</h2>
                <h3>User Information</h3>
                <div className="form-group">
                  <label>Name:</label>
                  <input
                    type="text"
                    name="name"
                    value={fullRegistrationData.userDto.name}
                    onChange={(e) => handleFullRegistrationChange('userDto', e)}
                    autoComplete="name"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Email:</label>
                  <input
                    type="email"
                    name="email"
                    value={fullRegistrationData.userDto.email}
                    onChange={(e) => handleFullRegistrationChange('userDto', e)}
                    autoComplete="email"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Password:</label>
                  <input
                    type="password"
                    name="password"
                    value={fullRegistrationData.userDto.password}
                    onChange={(e) => handleFullRegistrationChange('userDto', e)}
                    autoComplete="new-password"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Role:</label>
                  <select
                    name="role"
                    value={fullRegistrationData.userDto.role}
                    onChange={(e) => handleFullRegistrationChange('userDto', e)}
                  >
                    <option value="CUSTOMER">Customer</option>
                    <option value="ADMIN">Admin</option>
                  </select>
                </div>

                <h3>Customer Information</h3>
                <div className="form-group">
                  <label>Address:</label>
                  <input
                    type="text"
                    name="address"
                    value={fullRegistrationData.customerDto.address}
                    onChange={(e) => handleFullRegistrationChange('customerDto', e)}
                    autoComplete="street-address"
                    required
                  />
                </div>
                <div className="form-group">
                  <label>Phone:</label>
                  <input
                    type="tel"
                    name="phone"
                    value={fullRegistrationData.customerDto.phone}
                    onChange={(e) => handleFullRegistrationChange('customerDto', e)}
                    autoComplete="tel"
                    required
                  />
                </div>
                <button type="submit" disabled={loading}>
                  {loading ? 'Registering...' : 'Complete Registration'}
                </button>
              </form>
            )}
          </div>
        </div>
      ) : (
        <div className="authenticated-view">
          <h2>Welcome! You are logged in.</h2>
          <p>Token: {token.substring(0, 20)}...</p>
        </div>
      )}
    </div>
  );
};

export default AuthSystem;