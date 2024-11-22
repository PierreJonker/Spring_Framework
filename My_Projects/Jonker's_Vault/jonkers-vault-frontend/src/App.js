// src/App.js
import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import axios from 'axios';
import NavbarComponent from './components/NavbarComponent.js';
import LoginPage from './components/LoginPage.js';
import SignupPage from './components/SignupPage.js';
import HomePage from './components/HomePage.js';
import ProfilePage from './components/ProfilePage.js';
import TransactionsPage from './components/TransactionsPage.js'; // Import TransactionsPage
import FinancialGoalsPage from './components/FinancialGoalsPage.js'; // Import FinancialGoalsPage
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import './App.css';

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isLoading, setIsLoading] = useState(true);

    // Function to update authentication state from the LoginPage
    const handleAuthChange = (status) => {
        setIsAuthenticated(status);
    };

    // Check authentication on initial load
    useEffect(() => {
        const checkAuth = async () => {
            try {
                const token = localStorage.getItem('token');
                if (token) {
                    const response = await axios.get('http://localhost:8080/api/auth/user-details', {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    });
                    if (response.status === 200) {
                        setIsAuthenticated(true);
                        localStorage.setItem('isAuthenticated', 'true');
                    }
                }
            } catch (error) {
                setIsAuthenticated(false);
                localStorage.removeItem('isAuthenticated');
            } finally {
                setIsLoading(false);
            }
        };

        checkAuth();
    }, []); // Only run on initial load

    // If still loading authentication, show a loading indicator
    if (isLoading) {
        return <div>Loading...</div>;
    }

    // Protected route component to handle authenticated access
    const ProtectedRoute = ({ element }) => {
        return isAuthenticated ? element : <Navigate to="/login" />;
    };

    return (
        <Router>
            <NavbarComponent isAuthenticated={isAuthenticated} />
            <Routes>
                <Route
                    path="/login"
                    element={!isAuthenticated ? <LoginPage onAuthChange={handleAuthChange} /> : <Navigate to="/profile" />}
                />
                <Route path="/signup" element={<SignupPage />} />
                <Route path="/" element={<HomePage />} />
                <Route path="/profile" element={<ProtectedRoute element={<ProfilePage />} />} />
                <Route path="/transactions" element={<ProtectedRoute element={<TransactionsPage />} />} />
                <Route path="/financial-goals" element={<ProtectedRoute element={<FinancialGoalsPage />} />} /> {/* Financial Goals Page */}
            </Routes>
            <ToastContainer />
        </Router>
    );
}

export default App;
