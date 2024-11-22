import React, { useState, useEffect, useCallback } from 'react';
import { Form, Button, Table, Modal } from 'react-bootstrap';
import axios from 'axios';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import '../App.css';

const FinancialGoalsPage = () => {
    const [goalForm, setGoalForm] = useState({ goalName: '', targetAmount: '', currentAmount: '', targetDate: '', id: '' });
    const [goals, setGoals] = useState([]);
    const [showDeleteModal, setShowDeleteModal] = useState(false); // Modal state
    const [selectedGoalId, setSelectedGoalId] = useState(null); // Track the goal being deleted

    // Memoize fetchGoals using useCallback
    const fetchGoals = useCallback(async () => {
        try {
            const token = getAuthToken();
            const response = await axios.get('http://localhost:8080/api/goals', {
                headers: { Authorization: `Bearer ${token}` },
            });
            setGoals(response.data);
        } catch (error) {
            toast.error('Error fetching financial goals.', { position: 'top-right' });
        }
    }, []);

    // Get authentication token
    const getAuthToken = () => {
        return localStorage.getItem('token');
    };

    // Fetch goals on initial render or when fetchGoals changes
    useEffect(() => {
        fetchGoals();
    }, [fetchGoals]);

    const handleGoalSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = getAuthToken();
            const updatedGoal = { ...goalForm };
    
            if (goalForm.id) {
                await axios.put(`http://localhost:8080/api/goals/${goalForm.id}`, updatedGoal, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                toast.success('Goal updated successfully!', { position: 'top-right' });
            } else {
                await axios.post('http://localhost:8080/api/goals', updatedGoal, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                toast.success('Goal added successfully!', { position: 'top-right' });
            }
            
            setGoalForm({ goalName: '', targetAmount: '', currentAmount: '', targetDate: '', id: '' });
            fetchGoals();
        } catch (error) {
            toast.error('Error saving goal.', { position: 'top-right' });
        }
    };

    const handleDeleteGoal = async () => {
        try {
            const token = getAuthToken();
            await axios.delete(`http://localhost:8080/api/goals/${selectedGoalId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            toast.success('Goal deleted!', { position: 'top-right' });
            fetchGoals();
            setShowDeleteModal(false); // Close the modal after deletion
        } catch (error) {
            toast.error('Error deleting goal.', { position: 'top-right' });
        }
    };

    const handleEditGoal = (goal) => {
        setGoalForm({
            goalName: goal.goalName,
            targetAmount: goal.targetAmount,
            currentAmount: goal.currentAmount,
            targetDate: goal.targetDate,
            id: goal.id,
        });
    };

    const openDeleteModal = (goalId) => {
        setSelectedGoalId(goalId); // Set the goal ID to be deleted
        setShowDeleteModal(true);  // Open the modal
    };

    return (
        <div className="container">
            {/* Form Section */}
            <div className="form-container" style={{ maxWidth: '800px', marginBottom: '40px' }}>
                <h2 className="title" style={{ marginBottom: '30px' }}>Manage Financial Goals</h2>
                
                <Form onSubmit={handleGoalSubmit}>
                    <Form.Group className="mb-4">
                        <Form.Label>Goal Name</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Enter goal name"
                            value={goalForm.goalName}
                            onChange={(e) => setGoalForm({ ...goalForm, goalName: e.target.value })}
                            required
                        />
                    </Form.Group>
                    <Form.Group className="mb-4">
                        <Form.Label>Target Amount</Form.Label>
                        <Form.Control
                            type="number"
                            placeholder="Enter target amount"
                            value={goalForm.targetAmount}
                            onChange={(e) => setGoalForm({ ...goalForm, targetAmount: e.target.value })}
                            required
                        />
                    </Form.Group>
                    <Form.Group className="mb-4">
                        <Form.Label>Current Amount</Form.Label>
                        <Form.Control
                            type="number"
                            placeholder="Enter current amount"
                            value={goalForm.currentAmount}
                            onChange={(e) => setGoalForm({ ...goalForm, currentAmount: e.target.value })}
                            required
                        />
                    </Form.Group>
                    <Form.Group className="mb-4">
                        <Form.Label>Target Date</Form.Label>
                        <Form.Control
                            type="date"
                            value={goalForm.targetDate}
                            onChange={(e) => setGoalForm({ ...goalForm, targetDate: e.target.value })}
                            required
                        />
                    </Form.Group>
                    <Button 
                        type="submit" 
                        variant="primary" 
                        className="w-100"
                        style={{ backgroundColor: '#0d6efd', border: 'none' }}
                    >
                        {goalForm.id ? 'Update Goal' : 'Add Goal'}
                    </Button>
                </Form>
            </div>

            {/* Table Section */}
            <div className="table-container" style={{ maxWidth: '1200px', margin: '0 auto' }}>
                <Table striped bordered hover responsive>
                    <thead>
                        <tr>
                            <th>Goal Name</th>
                            <th>Target Amount</th>
                            <th>Current Amount</th>
                            <th>Target Date</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {goals.map((goal) => (
                            <tr key={goal.id}>
                                <td>{goal.goalName}</td>
                                <td>{`R ${goal.targetAmount.toFixed(2)}`}</td>
                                <td>{`R ${goal.currentAmount.toFixed(2)}`}</td>
                                <td>{goal.targetDate}</td>
                                <td>
                                    <div className="d-flex justify-content-center gap-2">
                                        <Button 
                                            variant="success" 
                                            size="sm" 
                                            onClick={() => handleEditGoal(goal)} 
                                            style={{ marginRight: '10px' }}
                                        >
                                            Edit
                                        </Button>
                                        <Button 
                                            variant="danger" 
                                            size="sm" 
                                            onClick={() => openDeleteModal(goal.id)}
                                        >
                                            Delete
                                        </Button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            </div>

            {/* Confirmation Modal for Deletion */}
            <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Confirm Deletion</Modal.Title>
                </Modal.Header>
                <Modal.Body>Are you sure you want to delete this goal? This action cannot be undone.</Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={handleDeleteGoal}>
                        Confirm Deletion
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
};

export default FinancialGoalsPage;
