// src/components/SignupPage.js
import React, { useState } from 'react';
import { Form, Button, Container } from 'react-bootstrap';
import axios from 'axios';
import '../App.css'; // Custom styles

const SignupPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [dateOfBirth, setDateOfBirth] = useState('');

    // Helper function to format the date to YYYY-MM-DD
    const formatDate = (date) => {
        if (!date) return '';
        const dateObject = new Date(date);
        const year = dateObject.getFullYear();
        const month = String(dateObject.getMonth() + 1).padStart(2, '0'); // Months are zero-based
        const day = String(dateObject.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Format the date to YYYY-MM-DD
        const formattedDate = formatDate(dateOfBirth);

        // Debugging: Log formatted date to ensure it's correct
        console.log('Formatted Date:', formattedDate);

        if (!formattedDate) {
            alert('Please enter a valid date in the format YYYY-MM-DD.');
            return;
        }

        try {
            const response = await axios.post('http://localhost:8080/api/auth/signup', {
                email,
                password,
                birthDate: formattedDate, // Use camel case "birthDate" to match the backend
            });
            if (response.status === 200) {
                alert('Signup successful!');
            } else {
                alert('Signup failed. Please try again.');
            }
        } catch (error) {
            if (error.response && error.response.data) {
                alert(`Signup failed: ${error.response.data.message}`);
            } else {
                console.error('Error during signup:', error);
                alert('An error occurred. Please try again.');
            }
        }
    };

    return (
        <Container className="mt-5">
            <h2>Sign Up</h2>
            <Form onSubmit={handleSubmit}>
                <Form.Group controlId="formBasicEmail">
                    <Form.Label>Email address</Form.Label>
                    <Form.Control
                        type="email"
                        placeholder="Enter email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </Form.Group>

                <Form.Group controlId="formBasicPassword" className="mt-3">
                    <Form.Label>Password</Form.Label>
                    <Form.Control
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </Form.Group>

                <Form.Group controlId="formBasicDateOfBirth" className="mt-3">
                    <Form.Label>Date of Birth</Form.Label>
                    <Form.Control
                        type="date"
                        value={dateOfBirth}
                        onChange={(e) => setDateOfBirth(e.target.value)}
                        required
                    />
                </Form.Group>

                <Button variant="primary" type="submit" className="mt-4">
                    Sign Up
                </Button>
            </Form>
        </Container>
    );
};

export default SignupPage;
