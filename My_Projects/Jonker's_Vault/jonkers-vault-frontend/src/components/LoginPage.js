import React, { useState } from 'react';
import { Form, Button, Container } from 'react-bootstrap';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import '../App.css';

const LoginPage = ({ onAuthChange }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/auth/login', {
                email,
                password,
            }, {
                withCredentials: true
            });

            if (response.status === 200) {
                toast.success('✅ Login successful!', {
                    position: "top-right",
                    autoClose: 3000,
                });

                const { token } = response.data;
                console.log('Token received from backend:', token);

                // Store token in localStorage
                localStorage.setItem('token', token);
                localStorage.setItem('isAuthenticated', 'true');
                localStorage.setItem('userEmail', email);

                // Update the authentication status in the parent component (App.js)
                onAuthChange(true); // Set isAuthenticated to true

                // Navigate to the profile page
                setTimeout(() => {
                    console.log('Navigating to profile...');
                    navigate('/profile');
                }, 300);
            }
        } catch (error) {
            console.error('Login error:', error);
            toast.error('⚠️ Error: Please check your credentials and try again.', {
                position: "top-right",
                autoClose: 3000,
            });
        }
    };

    return (
        <Container className="login-page">
            <div className="form-container">
                <h2 className="title">Login</h2>
                <Form onSubmit={handleLogin}>
                    <Form.Group controlId="formBasicEmail" className="form-group">
                        <Form.Label>Email address</Form.Label>
                        <Form.Control
                            type="email"
                            placeholder="Enter email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                            className="form-control"
                        />
                    </Form.Group>

                    <Form.Group controlId="formBasicPassword" className="form-group">
                        <Form.Label>Password</Form.Label>
                        <Form.Control
                            type="password"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            className="form-control"
                        />
                    </Form.Group>

                    <Button variant="primary" type="submit" className="form-button">
                        Login
                    </Button>
                </Form>
            </div>
        </Container>
    );
};

export default LoginPage;
