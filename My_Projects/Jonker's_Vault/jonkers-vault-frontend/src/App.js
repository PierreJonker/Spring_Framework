// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import NavbarComponent from './components/NavbarComponent';
import LoginPage from './components/LoginPage';
import SignupPage from './components/SignupPage';
import './App.css';

function App() {
    return (
        <Router>
            <NavbarComponent />
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/signup" element={<SignupPage />} />
                <Route
                    path="/"
                    element={
                        <div className="App-header">
                            <h1>Welcome to Jonker's Vault</h1>
                            <p>Manage your finances efficiently and securely.</p>
                        </div>
                    }
                />
            </Routes>
        </Router>
    );
}

export default App;
