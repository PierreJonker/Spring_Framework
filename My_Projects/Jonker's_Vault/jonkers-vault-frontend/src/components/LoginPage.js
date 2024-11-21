import React, { useState } from 'react';
import { Form, Button, Container } from 'react-bootstrap';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import '../App.css';

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/auth/login', {
                email,
                password,
            });
    
            if (response.status === 200) {
                toast.success('✅ Login successful!', {
                    position: "top-right",
                    autoClose: 3000,
                });
    
                // Log the response to see if the frontend gets the token
                console.log("Response from Backend:", response); // Logs the entire response, including the token
    
                // Extract token from response and store it in localStorage
                const token = response.data.token.split(' ')[1]; // Remove 'Bearer ' from the token
    
                console.log("JWT Token stored in localStorage:", token); // This will print the token to the console
    
                localStorage.setItem('token', token); // Store token
                localStorage.setItem('isAuthenticated', 'true'); // Set auth status
    
                // Optionally store user information if needed
                localStorage.setItem('userEmail', email);
    
                navigate('/profile'); // Redirect to the profile or dashboard page
            }
        } catch (error) {
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
