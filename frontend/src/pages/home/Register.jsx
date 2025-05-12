import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import './styles/Auth.css';

const Register = ({ onRegister }) => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
    role: 'CUSTOMER' // Default role set to CUSTOMER
  });
  const [generalError, setGeneralError] = useState(''); // For general error messages
  const [fieldErrors, setFieldErrors] = useState({}); // For field-specific errors from backend
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevData => ({
      ...prevData,
      [name]: value
    }));

    // Clear field-specific error when user starts typing in that field
    if (fieldErrors[name]) {
      setFieldErrors(prevErrors => ({
        ...prevErrors,
        [name]: undefined // Or delete prevErrors[name]
      }));
    }
    // Clear general error when user interacts with the form
    if (generalError) {
      setGeneralError('');
    }
  };

  const validateForm = () => {
    let isValid = true;
    const newFieldErrors = {};

    if (!formData.name.trim()) {
      newFieldErrors.name = 'Full Name is required.';
      isValid = false;
    }
    if (!formData.email.trim()) {
      newFieldErrors.email = 'Email is required.';
      isValid = false;
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newFieldErrors.email = 'Email address is invalid.';
      isValid = false;
    }
    if (!formData.password) {
      newFieldErrors.password = 'Password is required.';
      isValid = false;
    } else if (formData.password.length < 6) {
      newFieldErrors.password = 'Password must be at least 6 characters long.';
      isValid = false;
    }
    if (!formData.confirmPassword) {
      newFieldErrors.confirmPassword = 'Confirm Password is required.';
      isValid = false;
    } else if (formData.password !== formData.confirmPassword) {
      setGeneralError('Passwords do not match.'); // Or use fieldErrors for confirmPassword
      // newFieldErrors.confirmPassword = 'Passwords do not match.';
      isValid = false;
    }
    setFieldErrors(newFieldErrors);
    return isValid;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setGeneralError('');
    setFieldErrors({});

    if (!validateForm()) {
      setIsLoading(false);
      return;
    }
    setIsLoading(true);

    const payload = {
      name: formData.name,
      email: formData.email,
      password: formData.password,
      role: formData.role // Include the role in the payload
    };
    // console.log("Sending payload to backend:", JSON.stringify(payload));

    try {
      const response = await fetch('http://localhost:8080/auth/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          // Optionally, you can add an 'Accept' header, though Spring Boot is usually good at inferring
          // 'Accept': 'application/json',
        },
        body: JSON.stringify(payload)
      });

      // Get the raw response body as text first, regardless of status
      const responseText = await response.text();

      if (response.ok) { // Status 200-299
        try {
          const responseData = JSON.parse(responseText); // Now parse the text if response was OK
          // console.log('Registration successful:', responseData);
          if (onRegister && typeof onRegister === 'function') {
            onRegister(responseData);
          }
          navigate('/login', { state: { message: 'Registration successful! Please log in.' } });
        } catch (parseError) {
          console.error('Failed to parse JSON from successful response:', parseError, "\nResponse Text:", responseText);
          setGeneralError('Registration was successful, but the server response was not in the expected format.');
          navigate('/login'); // Or handle appropriately
        }
      } else { // HTTP error (e.g., 400, 500)
        // console.error(`HTTP error ${response.status}. Response text was:`, responseText);
        let parsedErrorData;
        try {
          parsedErrorData = JSON.parse(responseText); // Attempt to parse error response as JSON
        } catch (e) {
          // If responseText is not valid JSON, parsedErrorData will be undefined
          // console.warn('Error response body was not valid JSON.', e);
        }

        if (parsedErrorData && typeof parsedErrorData === 'object') {
          if (parsedErrorData.error) { // Backend sent {"error": "message"}
            setGeneralError(parsedErrorData.error);
          } else { // Backend sent field errors {"field1": "message1", "field2": "message2"}
            setFieldErrors(parsedErrorData);
            setGeneralError('Please correct the errors indicated below.');
          }
        } else {
          // Fallback if error response was not JSON or not in the expected structure
          if (responseText && responseText.toLowerCase().includes("<html")) {
            // Avoid displaying full HTML page as error message
            setGeneralError(`Registration failed (Status ${response.status}). The server returned an HTML error page. Please check server logs for details.`);
            // You could also try to extract a title or key message from the HTML if desired
            // For example: const titleMatch = responseText.match(/<title>(.*?)<\/title>/i);
            // if (titleMatch && titleMatch[1]) setGeneralError(titleMatch[1]);
          } else if (responseText && responseText.trim().length > 0) {
              // Show a snippet of the plain text error if it's not too long
            setGeneralError(responseText.substring(0, 250) + (responseText.length > 250 ? "..." : ""));
          } else {
            setGeneralError(`Registration failed with status: ${response.status}. No further details provided by the server.`);
          }
        }
      }
    } catch (err) { // This usually catches network errors or if fetch() itself throws
      console.error('Network error or issue with fetch operation during registration:', err);
      setGeneralError('An unexpected network error occurred. Please check your connection and try again.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>Create an Account</h2>

        {generalError && <div className="auth-error">{generalError}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="name">Full Name</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
            />
            {fieldErrors.name && <div className="error-message">{fieldErrors.name}</div>}
          </div>

          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
            {fieldErrors.email && <div className="error-message">{fieldErrors.email}</div>}
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
              minLength="6"
            />
            {fieldErrors.password && <div className="error-message">{fieldErrors.password}</div>}
          </div>

          <div className="form-group">
            <label htmlFor="confirmPassword">Confirm Password</label>
            <input
              type="password"
              id="confirmPassword"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              required
              minLength="6"
            />
            {fieldErrors.confirmPassword && <div className="error-message">{fieldErrors.confirmPassword}</div>}
          </div>

          {/* Optional: If you still want to allow users to choose a different role */}
          {/* <div className="form-group">
            <label htmlFor="role">Role</label>
            <select id="role" name="role" value={formData.role} onChange={handleChange} required>
              <option value="CUSTOMER">Customer</option>
              <option value="ADMIN">Admin</option>
              {/* Add other roles as needed */}
            {/* </select>
            {fieldErrors.role && <div className="error-message">{fieldErrors.role}</div>}
          </div> */}

          <button type="submit" className="auth-btn" disabled={isLoading}>
            {isLoading ? 'Registering...' : 'Register'}
          </button>
        </form>

        <div className="auth-footer">
          <span>Already have an account? </span>
          <Link to="/login">Login here</Link>
        </div>
      </div>
    </div>
  );
};

export default Register;