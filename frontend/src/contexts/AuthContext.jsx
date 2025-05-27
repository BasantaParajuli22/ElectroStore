"use client"

import { createContext, useContext, useState, useEffect } from "react"

const AuthContext = createContext(undefined)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null) // Keep user state, but it might be null initially after login
  const [token, setToken] = useState(null)
  const [isLoading, setIsLoading] = useState(true)

  // Function to fetch user profile using the token
  const fetchUserProfile = async (jwtToken) => {
    try {
      const response = await fetch("http://localhost:8080/api/users/me", {
        headers: {
          "Authorization": `Bearer ${jwtToken}`, // Send the token in the Authorization header
        },
      });

      if (!response.ok) {
        // If profile fetch fails (e.g., token expired, invalid)
        console.error("Failed to fetch user profile:", response.statusText);
        logout(); // Log out the user if profile fetch fails
        return null;
      }

      const userData = await response.json();
      return userData;
    } catch (error) {
      console.error("Error fetching user profile:", error);
      logout(); // Log out on network error
      return null;
    }
  };

  useEffect(() => {
    const storedToken = localStorage.getItem("electromart_token")
    if (storedToken) {
      setToken(storedToken)
      // If a token exists, try to fetch user profile
      fetchUserProfile(storedToken).then((userData) => {
        if (userData) {
          setUser(userData);
          // Store user data in localStorage too, so it persists across sessions
          localStorage.setItem("electromart_user", JSON.stringify(userData));
        }
      });
    }
    setIsLoading(false)
  }, [])


  const login = async (jwtToken) => { // Make login async
    setToken(jwtToken)
    localStorage.setItem("electromart_token", jwtToken)

    const userData = await fetchUserProfile(jwtToken); // Fetch user data after getting token
    if (userData) {
      setUser(userData);
      localStorage.setItem("electromart_user", JSON.stringify(userData));
    } else {
      setUser(null); // Clear user if fetching failed
      localStorage.removeItem("electromart_user");
    }
  }

  const logout = () => {
    setUser(null)
    setToken(null)
    localStorage.removeItem("electromart_user")
    localStorage.removeItem("electromart_token")
  }

  const isAuthenticated = () => {
    if (!token) {
      return false
    }
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const tokenData = JSON.parse(atob(base64));

      if (tokenData.exp) {
        const expirationTime = tokenData.exp * 1000;
        return Date.now() < expirationTime;
      }
      return true;
    } catch (e) {
      console.error("Error decoding or validating token:", e);
      return false;
    }
  };

  return (
    <AuthContext.Provider value={{ user, token, login, logout, isLoading, isAuthenticated }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider")
  }
  return context
}