import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import axios from 'axios';
import NavbarComponent from './components/NavbarComponent.js';
import LoginPage from './components/LoginPage.js';
import SignupPage from './components/SignupPage.js';
import HomePage from './components/HomePage.js';
import ProfilePage from './components/ProfilePage.js';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import './App.css';

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const checkAuth = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/auth/user-details', {
                    withCredentials: true,
                });
                if (response.status === 200) {
                    setIsAuthenticated(true);
                    sessionStorage.setItem('isAuthenticated', 'true');
                }
            } catch (error) {
                setIsAuthenticated(false);
                sessionStorage.removeItem('isAuthenticated');
            } finally {
                setIsLoading(false);
            }
        };

        checkAuth();
    }, []);

    // Add axios default configuration
    axios.defaults.withCredentials = true;

    if (isLoading) {
        return <div>Loading...</div>; // Or your loading component
    }

    const ProtectedRoute = ({ element }) => {
        return isAuthenticated ? element : <Navigate to="/login" />;
    };

    return (
        <Router>
            <NavbarComponent isAuthenticated={isAuthenticated} />
            <Routes>
                <Route path="/login" element={!isAuthenticated ? <LoginPage /> : <Navigate to="/profile" />} />
                <Route path="/signup" element={<SignupPage />} />
                <Route path="/" element={<HomePage />} />
                <Route path="/profile" element={<ProtectedRoute element={<ProfilePage />} />} />
            </Routes>
            <ToastContainer />
        </Router>
    );
}

export default App;
