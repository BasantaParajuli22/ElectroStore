import { useState } from "react"
import { useAuth } from "../contexts/AuthContext"
import "./AuthModal.css"

function AuthModal({ isOpen, onClose, initialMode = "login" }) {
  const [mode, setMode] = useState(initialMode)
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState(null)
  const { login } = useAuth();//calling useAuth() 

  const [loginData, setLoginData] = useState({ email: "", password: "" })
  const [registerData, setRegisterData] = useState({
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
    role: ""
  })

  const API_BASE_URL = "http://localhost:8080/api/auth";

  const handleLoginSubmit = async (e) => {
    e.preventDefault()
    setIsLoading(true)
    setError(null)

    try {
      const response = await fetch(`${API_BASE_URL}/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(loginData),
      })

      const data = await response.json()
      console.log("Login API Response:", data); // Keep this for debugging!

      if (!response.ok) {
        throw new Error(data.message || "Invalid credentials. Please try again.")
      }

      // *** FIX START HERE ***
      // Backend returns "accessToken", so we need to use that key
      if (data.accessToken) {
        login(data.accessToken); // Pass the accessToken to the login function
        onClose();
        setLoginData({ email: "", password: "" });
      } else {
        throw new Error("No accessToken received from the server."); // Updated error message
      }
      // *** FIX END HERE ***
    } catch (err) {
      setError(err.message)
    } finally {
      setIsLoading(false)
    }
  }

  //handling user registering
  const handleRegisterSubmit = async (e) => {
    e.preventDefault()
    setError(null)

    //frontend password and confirm password check
    if (registerData.password !== registerData.confirmPassword) {
      setError("Passwords do not match.")
      return
    }

    setIsLoading(true)

    try {
      const response = await fetch(`${API_BASE_URL}/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: registerData.username,
          email: registerData.email,
          password: registerData.password,
          role:"CUSTOMER",
        }),
      })

      const data = await response.json()
      console.log("Register API Response:", data); // Keep this for debugging!

      if (!response.ok) {
        throw new Error(data.message || "Registration failed. Email might be already in use.")
      }

      // *** FIX START HERE ***
      // Assuming register also returns "accessToken"
      if (data.accessToken) {
        // If your register endpoint returns user details *in addition* to the token,
        // e.g., { user: {...}, accessToken: "..." }, you can pass it here:
        // login(data.accessToken, data.user);

        // If register only returns accessToken (like login), then:
        login(data.accessToken); // Pass ONLY the accessToken
        onClose();
        setRegisterData({
          username: "",
          email: "",
          password: "",
          confirmPassword: "",
          role: "",
        });
      } else {
        throw new Error("No accessToken received after registration."); // Updated error message
      }
      // *** FIX END HERE ***
    } catch (err) {
      setError(err.message)
    } finally {
      setIsLoading(false)
    }
  }

  const handleLoginChange = (e) => {
    setLoginData((prev) => ({ ...prev, [e.target.name]: e.target.value }))
  }

  const handleRegisterChange = (e) => {
    setRegisterData((prev) => ({ ...prev, [e.target.name]: e.target.value }))
  }

  const handleOverlayClick = (e) => {
    if (e.target === e.currentTarget) {
      onClose()
    }
  }

  if (!isOpen) return null

  return (
    <div className="modal-overlay" onClick={handleOverlayClick}>
      <div className="modal-wrapper">
        <div className="modal-content" onClick={(e) => e.stopPropagation()}>
          <button className="modal-close" onClick={onClose} aria-label="Close modal">
            ×
          </button>

          <div className="modal-header">
            <div className="mode-tabs">
              <button
                className={`mode-tab ${mode === "login" ? "active" : ""}`}
                onClick={() => setMode("login")}
              >
                Login
              </button>
              <button
                className={`mode-tab ${mode === "register" ? "active" : ""}`}
                onClick={() => setMode("register")}
              >
                Register
              </button>
            </div>
          </div>

          {mode === "login" ? (
            <div className="auth-form-container">
              <div className="form-header">
                <h2>Welcome Back</h2>
                <p>Sign in to your ElectroMart account</p>
              </div>
              <form onSubmit={handleLoginSubmit} className="auth-form">
                <div className="form-group">
                  <label htmlFor="login-email">Email</label>
                  <input
                    id="login-email"
                    name="email"
                    type="email"
                    placeholder="john@example.com"
                    value={loginData.email}
                    onChange={handleLoginChange}
                    required
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="login-password">Password</label>
                  <input
                    id="login-password"
                    name="password"
                    type="password"
                    placeholder="Enter your password"
                    value={loginData.password}
                    onChange={handleLoginChange}
                    required
                  />
                </div>
                {error && <p className="error-message">{error}</p>}
                <button type="submit" className="auth-btn" disabled={isLoading}>
                  {isLoading ? "Signing in..." : "Sign In"}
                </button>
              </form>
            </div>
          ) : (
            <div className="auth-form-container">
              <div className="form-header">
                <h2>Create Account</h2>
                <p>Join ElectroMart today</p>
              </div>
              <form onSubmit={handleRegisterSubmit} className="auth-form">
                <div className="form-row">
                  <div className="form-group">
                    <label htmlFor="register-firstName">Username</label>
                    <input
                      id="register-firstName"
                      name="username"
                      placeholder="John"
                      value={registerData.username}
                      onChange={handleRegisterChange}
                      required
                    />
                  </div>
                  {/* <div className="form-group">
                    <label htmlFor="register-lastName">Last Name</label>
                    <input
                      id="register-lastName"
                      name="lastName"
                      placeholder="Doe"
                      value={registerData.lastName}
                      onChange={handleRegisterChange}
                      required
                    />
                  </div> */}
                </div>
                <div className="form-group">
                  <label htmlFor="register-email">Email</label>
                  <input
                    id="register-email"
                    name="email"
                    type="email"
                    placeholder="john@example.com"
                    value={registerData.email}
                    onChange={handleRegisterChange}
                    required
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="register-password">Password</label>
                  <input
                    id="register-password"
                    name="password"
                    type="password"
                    placeholder="Create a password"
                    value={registerData.password}
                    onChange={handleRegisterChange}
                    required
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="register-confirmPassword">Confirm Password</label>
                  <input
                    id="register-confirmPassword"
                    name="confirmPassword"
                    type="password"
                    placeholder="Confirm your password"
                    value={registerData.confirmPassword}
                    onChange={handleRegisterChange}
                    required
                  />
                </div>
                {error && <p className="error-message">{error}</p>}
                <button type="submit" className="auth-btn" disabled={isLoading}>
                  {isLoading ? "Creating account..." : "Create Account"}
                </button>
              </form>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default AuthModal;