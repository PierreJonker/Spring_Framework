// src/components/NavbarComponent.js
import React from 'react';
import { Navbar, Nav, Container } from 'react-bootstrap';
import { useLocation } from 'react-router-dom';
import logo from '../Images/Jonker\'s_Vault.png'; // Update this path as needed

const NavbarComponent = () => {
    const location = useLocation();

    // Check the current path
    const isSignupPage = location.pathname === '/signup';
    const isLoginPage = location.pathname === '/login';

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
                        {/* Only hide the Login link on the Login Page */}
                        {!isLoginPage && <Nav.Link href="/login">Login</Nav.Link>}
                        {/* Only hide the Sign Up link on the Sign Up Page */}
                        {!isSignupPage && <Nav.Link href="/signup">Sign Up</Nav.Link>}
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};

export default NavbarComponent;
