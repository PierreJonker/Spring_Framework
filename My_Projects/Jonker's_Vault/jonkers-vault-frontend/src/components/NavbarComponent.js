import React from 'react';
import { Navbar, Nav, Container } from 'react-bootstrap';
import { useLocation, useNavigate } from 'react-router-dom';
import logo from '../Images/Jonker\'s_Vault.png';
import axios from 'axios';

const NavbarComponent = () => {
    const location = useLocation();
    const navigate = useNavigate();

    // Check if the user is logged in by checking localStorage for the token
    const isAuthenticated = localStorage.getItem('token') !== null;

    // Handle logout
    const handleLogout = async () => {
        try {
            // Make a logout API call, sending the token in Authorization header
            await axios.delete('http://localhost:8080/api/auth/logout', {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`, // Send JWT token for logout
                },
            });
            // Remove the token and user info from localStorage
            localStorage.removeItem('token');
            localStorage.removeItem('isAuthenticated');
            navigate('/login'); // Redirect to the login page
        } catch (error) {
            console.error("Logout failed", error);
        }
    };

    return (
        <Navbar bg="dark" variant="dark" expand="lg" collapseOnSelect className="navbar">
            <Container>
                <Navbar.Brand href="/">
                    <img
                        src={logo}
                        alt="Jonker's Vault Logo"
                        width="50"
                        height="50"
                        className="d-inline-block align-top"
                    />{' '}
                    <span className="navbar-title">Jonker's Vault</span>
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="ms-auto">
                        {!isAuthenticated && location.pathname !== '/login' && <Nav.Link href="/login">Login</Nav.Link>}
                        {!isAuthenticated && location.pathname !== '/signup' && <Nav.Link href="/signup">Sign Up</Nav.Link>}
                        {isAuthenticated && <Nav.Link href="/profile">Profile</Nav.Link>} {/* Profile link */}
                        {isAuthenticated && (
                            <Nav.Link onClick={handleLogout} style={{ cursor: 'pointer' }}>
                                Logout
                            </Nav.Link>
                        )}
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};

export default NavbarComponent;
