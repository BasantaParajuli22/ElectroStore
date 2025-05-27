import { useEffect, useState } from 'react';
import { useAuth } from '../contexts/AuthContext'; // Adjust path as needed
import { useNavigate } from 'react-router-dom'; // Correct import for React Router
import './ProfilePage.css'; // Make sure this CSS file exists

function ProfilePage() {
  const { user, token, isAuthenticated, isLoading, logout } = useAuth();
  const navigate = useNavigate(); // Correct hook for React Router
  const [profileData, setProfileData] = useState(null);
  const [loadingProfile, setLoadingProfile] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (isLoading) return; // Wait until auth state is loaded

    if (!isAuthenticated()) {
      navigate('/login'); // Correct usage: directly call navigate function
      return;
    }

    const fetchUserProfile = async () => {
      try {
        setLoadingProfile(true);
        setError(null);

        // This assumes your /api/users/me endpoint returns the full user details
        const response = await fetch("http://localhost:8080/api/users/me", {
          headers: {
            "Authorization": `Bearer ${token}`, // Include the JWT token
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) {
          if (response.status === 401 || response.status === 403) {
            // Token expired or invalid, log out
            logout();
            navigate('/login'); // Correct usage
            throw new Error("Session expired or unauthorized. Please log in again.");
          }
          throw new Error(`Failed to fetch profile: ${response.statusText}`);
        }

        const data = await response.json();
        setProfileData(data);
      } catch (err) {
        setError(err.message);
        console.error("Error fetching user profile:", err);
      } finally {
        setLoadingProfile(false);
      }
    };

    // If user data is already in context (e.g., from AuthContext fetching it on login), use it directly
    // Otherwise, fetch it.
    if (user && user.firstName) { // Check for a unique property to ensure it's full user data
      setProfileData(user);
      setLoadingProfile(false);
    } else if (token) { // Only fetch if we have a token but not full user data (or if user is null)
      fetchUserProfile();
    }
  }, [isAuthenticated, isLoading, navigate, token, logout, user]); // Include user in dependencies

  if (isLoading || loadingProfile) {
    return <div className="profile-container">Loading profile...</div>;
  }

  if (error) {
    return <div className="profile-container error-message">Error: {error}</div>;
  }

  if (!profileData) {
    return <div className="profile-container">No profile data available.</div>;
  }

  return (
    <div className="profile-container">
      <h1>My Profile</h1>
      <div className="profile-details">
        <p><strong>Name:</strong> {profileData.username}</p>
        <p><strong>Email:</strong> {profileData.email}</p>
        <p><strong>Role:</strong> {profileData.role}</p>
        {/* Add more user details as they become available from your backend */}
        {/* <p><strong>Address:</strong> {profileData.address}</p> */}
        {/* <p><strong>Phone:</strong> {profileData.phone}</p> */}
      </div>
      {/* Example: A button to update profile (would open a modal/form) */}
      <button className="edit-profile-btn">Edit Profile</button>
    </div>
  );
}

export default ProfilePage;