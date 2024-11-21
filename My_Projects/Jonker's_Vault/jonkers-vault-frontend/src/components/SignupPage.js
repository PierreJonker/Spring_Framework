import React, { useState } from 'react';
import { Form, Button } from 'react-bootstrap';
import axios from 'axios';
import { toast } from 'react-toastify'; // Import toast
import 'react-toastify/dist/ReactToastify.css'; // Import toast styles
import { useNavigate } from 'react-router-dom'; // Import useNavigate for navigation
import '../App.css'; // Custom styles

const SignupPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [dateOfBirth, setDateOfBirth] = useState('');
    const [showPassword, setShowPassword] = useState(false); // Manage password visibility
    const navigate = useNavigate(); // Hook to navigate to the login page

    // Helper function to format the date to YYYY-MM-DD
    const formatDate = (date) => {
        if (!date) return '';
        const dateObject = new Date(date);
        const year = dateObject.getFullYear();
        const month = String(dateObject.getMonth() + 1).padStart(2, '0');
        const day = String(dateObject.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formattedDate = formatDate(dateOfBirth);

        if (!formattedDate) {
            toast.error('❌ Please enter a valid date in the format YYYY-MM-DD.', {
                position: "top-right",
                autoClose: 3000,
                style: { maxWidth: "400px" } // Ensures the message is not cut off
            });
            return;
        }

        try {
            const response = await axios.post('http://localhost:8080/api/auth/signup', {
                email,
                password,
                birthDate: formattedDate,
            });

            if (response.status === 200) {
                toast.success('✅ Signup successful!', {
                    position: "top-right",
                    autoClose: 3000,
                    style: { maxWidth: "400px" }
                });

                // Redirect to login page after successful signup
                setTimeout(() => {
                    navigate('/login');
                }, 1000); // Redirect after 1 second to allow the toast to show
            } else {
                toast.error('❌ Signup failed. Please try again.', {
                    position: "top-right",
                    autoClose: 3000,
                    style: { maxWidth: "400px" }
                });
            }
        } catch (error) {
            if (error.response && error.response.data) {
                toast.error(`⚠️ Signup failed: ${error.response.data.message}`, {
                    position: "top-right",
                    autoClose: 3000,
                    style: { maxWidth: "400px" }
                });
            } else {
                console.error('Error during signup:', error);
                toast.error('❌ An error occurred. Please try again.', {
                    position: "top-right",
                    autoClose: 3000,
                    style: { maxWidth: "400px" }
                });
            }
        }
    };

    return (
        <div className="form-container">
            <h2 className="title">Sign Up</h2>
            <Form onSubmit={handleSubmit}>
                <Form.Group controlId="formBasicEmail" className="mt-3">
                    <Form.Label className="form-label">Email address</Form.Label>
                    <Form.Control
                        type="email"
                        placeholder="Enter email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className="form-control"
                        required
                    />
                </Form.Group>

                <Form.Group controlId="formBasicPassword" className="mt-3">
                    <Form.Label className="form-label">Password</Form.Label>
                    <div className="password-input">
                        <Form.Control
                            type={showPassword ? "text" : "password"} // Toggle password visibility
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="form-control"
                            required
                        />
                        <i
                            className={`eye-icon ${showPassword ? "show" : "hide"}`}
                            onClick={() => setShowPassword(!showPassword)} // Toggle the eye icon click
                        >
                            👁️
                        </i>
                    </div>
                </Form.Group>

                <Form.Group controlId="formBasicDateOfBirth" className="mt-3">
                    <Form.Label className="form-label">Date of Birth</Form.Label>
                    <Form.Control
                        type="date"
                        value={dateOfBirth}
                        onChange={(e) => setDateOfBirth(e.target.value)}
                        className="form-control"
                        required
                    />
                </Form.Group>

                <Button variant="primary" type="submit" className="form-button mt-4">
                    Sign Up
                </Button>
            </Form>
        </div>
    );
};

export default SignupPage;
