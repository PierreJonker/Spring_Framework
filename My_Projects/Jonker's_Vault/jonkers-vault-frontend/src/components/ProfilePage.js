import React, { useState, useEffect } from 'react';
import { Form, Button, Modal } from 'react-bootstrap';
import axios from 'axios';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import '../App.css';

const ProfilePage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [dateOfBirth, setDateOfBirth] = useState('');
  const [showPassword, setShowPassword] = useState(false); // Manage password visibility
  const [showConfirmModal, setShowConfirmModal] = useState(false); // Show confirmation modal for update
  const [showDeleteModal, setShowDeleteModal] = useState(false); // Show confirmation modal for delete

  // Fetch user details when the component mounts
  useEffect(() => {
    const fetchUserDetails = async () => {
      try {
        const token = localStorage.getItem('token');  // Get token from localStorage
        const response = await axios.get('http://localhost:8080/api/auth/user-details', {
          headers: {
            'Authorization': `Bearer ${token}`,  // Attach token in the header
          }
        });
        const { email, birthDate } = response.data;
        setEmail(email);
        setDateOfBirth(birthDate);
      } catch (error) {
        toast.error('⚠️ Error fetching user details. Please try again.', {
          position: "top-right",
          autoClose: 3000,
        });
      }
    };
    fetchUserDetails();
  }, []);

  // Handle updating user details
  const handleUpdate = async (e) => {
    e.preventDefault();
    setShowConfirmModal(true);  // Show confirmation modal for update
  };

  // Handle deleting user account
  const handleDelete = () => {
    setShowDeleteModal(true);  // Show confirmation modal for delete
  };

  // Confirm update action
  const confirmUpdate = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.put('http://localhost:8080/api/auth/update', {
        email,
        password: password || undefined, // Only send the password if it's not empty
        birthDate: dateOfBirth,
      }, {
        headers: {
          'Authorization': `Bearer ${token}`, // Send the token here
        }
      });
      if (response.status === 200) {
        toast.success('✅ Profile updated successfully!', {
          position: "top-right",
          autoClose: 3000,
        });
      }
    } catch (error) {
      toast.error('❌ Error updating profile. Please try again.', {
        position: "top-right",
        autoClose: 3000,
      });
    }
    setShowConfirmModal(false); // Close modal after action
  };

  // Confirm delete action
  const confirmDelete = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.delete('http://localhost:8080/api/auth/delete', {
        headers: {
          'Authorization': `Bearer ${token}`, // Send the token here
        }
      });
      if (response.status === 200) {
        toast.success('✅ Account deleted successfully!', {
          position: "top-right",
          autoClose: 3000,
        });
        localStorage.removeItem('token'); // Remove the token
        window.location.href = '/signup'; // Redirect to the signup page after deletion
      }
    } catch (error) {
      toast.error('❌ Error deleting account. Please try again.', {
        position: "top-right",
        autoClose: 3000,
      });
    }
    setShowDeleteModal(false); // Close modal after action
  };

  return (
    <div className="form-container">
      <h2 className="title">Manage Profile</h2>
      <Form onSubmit={handleUpdate}>
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
              placeholder="Enter new password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="form-control"
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
          Update Profile
        </Button>
        <Button variant="danger" className="form-button mt-2" onClick={handleDelete}>
          Delete Account
        </Button>
      </Form>

      {/* Modal for confirmation of update */}
      <Modal show={showConfirmModal} onHide={() => setShowConfirmModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Confirm Update</Modal.Title>
        </Modal.Header>
        <Modal.Body>Are you sure you want to update your profile?</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowConfirmModal(false)}>
            Cancel
          </Button>
          <Button variant="primary" onClick={confirmUpdate}>
            Confirm Update
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Modal for confirmation of delete */}
      <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Confirm Deletion</Modal.Title>
        </Modal.Header>
        <Modal.Body>Are you sure you want to delete your account? This action cannot be undone.</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
            Cancel
          </Button>
          <Button variant="danger" onClick={confirmDelete}>
            Confirm Deletion
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default ProfilePage;
