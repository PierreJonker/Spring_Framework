// src/components/NavbarComponent.js
import React from 'react';
import { Navbar, Nav, Container } from 'react-bootstrap';
import { useLocation, useNavigate } from 'react-router-dom';
import logo from '../Images/Jonker\'s_Vault.png';
import axios from 'axios';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const NavbarComponent = ({ isAuthenticated }) => {
    const location = useLocation();
    const navigate = useNavigate();

    // Handle logout
    const handleLogout = async () => {
        try {
            await axios.delete('http://localhost:8080/api/auth/logout', {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`,
                },
            });
            localStorage.removeItem('token');
            localStorage.removeItem('isAuthenticated');
            toast.success('✅ Logout successful!', {
                position: 'top-right',
                autoClose: 3000,
            });
            // Redirect to the home page after logout using navigate
            navigate('/'); // This line is necessary to trigger the navigate function
            window.location.reload(); // Reload the page to update the state
        } catch (error) {
            console.error('Logout failed', error);
            toast.error('⚠️ Logout failed, please try again!', {
                position: 'top-right',
                autoClose: 3000,
            });
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
                        {isAuthenticated && <Nav.Link href="/profile">Profile</Nav.Link>}
                        {isAuthenticated && <Nav.Link href="/transactions">Transactions</Nav.Link>} {/* Transactions link */}
                        {isAuthenticated && <Nav.Link href="/financial-goals">Financial Goals</Nav.Link>} {/* Financial Goals link */}
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
