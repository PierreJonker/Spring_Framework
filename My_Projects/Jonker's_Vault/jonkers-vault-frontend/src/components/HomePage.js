// src/components/HomePage.js
import React from 'react';
import '../App.css';

function HomePage() {
    return (
        <div className="home-page">
            {/* Hero Section */}
            <header className="hero-section">
                <h1 className="title">Welcome to Jonker's Vault</h1>
                <p className="subtitle">Securely manage and track your finances in one place.</p>
            </header>

            {/* Features Section */}
            <section className="features-section">
                <h2 className="title">Key Features</h2>
                <ul className="features-list">
                    <li className="feature-item">Secure login and data encryption</li>
                    <li className="feature-item">Budget tracking and goal setting</li>
                    <li className="feature-item">Detailed transaction management</li>
                    <li className="feature-item">Responsive design for all devices</li>
                </ul>
            </section>

            {/* Benefits Section */}
            <section className="benefits-section">
                <h2 className="title">Why Use Jonker's Vault?</h2>
                <p className="benefits-text">
                    Jonker's Vault offers a comprehensive solution for managing your personal finances,
                    helping you stay on top of your budget and achieve your financial goals.
                </p>
            </section>

            {/* Footer Section */}
            <footer className="footer">
                <p>&copy; 2024 Jonker's Vault. All rights reserved.</p>
            </footer>
        </div>
    );
}

export default HomePage;
