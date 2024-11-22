import React, { useState, useEffect, useCallback } from 'react';
import { Form, Button, Table, Tabs, Tab, Modal } from 'react-bootstrap';
import axios from 'axios';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import '../App.css';

const TransactionsPage = () => {
    const [activeTab, setActiveTab] = useState('transactions');
    const [transactions, setTransactions] = useState([]);
    const [budgets, setBudgets] = useState([]);
    const [categories, setCategories] = useState([]);
    const [transactionForm, setTransactionForm] = useState({ description: '', amount: '', date: '', categoryId: '', id: '' });
    const [budgetForm, setBudgetForm] = useState({ categoryId: '', amount: '', startDate: '', endDate: '', id: '' });
    const [showModal, setShowModal] = useState(false);
    const [selectedItemId, setSelectedItemId] = useState(null);
    const [deleteType, setDeleteType] = useState('');

    const getAuthToken = () => {
        return localStorage.getItem('token');
    };

    const fetchTransactions = useCallback(async () => {
        try {
            const token = getAuthToken();
            const response = await axios.get('http://localhost:8080/api/transactions', {
                headers: { Authorization: `Bearer ${token}` },
            });
            setTransactions(response.data);
        } catch (error) {
            toast.error('Error fetching transactions.', { position: 'top-right' });
        }
    }, []);

    const fetchBudgets = useCallback(async () => {
        try {
            const token = getAuthToken();
            const response = await axios.get('http://localhost:8080/api/budgets', {
                headers: { Authorization: `Bearer ${token}` },
            });
            setBudgets(response.data);
        } catch (error) {
            toast.error('Error fetching budgets.', { position: 'top-right' });
        }
    }, []);

    const fetchCategories = useCallback(async () => {
        try {
            const token = getAuthToken();
            const response = await axios.get('http://localhost:8080/api/categories', {
                headers: { Authorization: `Bearer ${token}` },
            });
            setCategories(response.data);
        } catch (error) {
            toast.error('Error fetching categories.', { position: 'top-right' });
        }
    }, []);

    useEffect(() => {
        const fetchAllData = async () => {
            await Promise.all([fetchTransactions(), fetchBudgets(), fetchCategories()]);
        };
        fetchAllData();
    }, [fetchTransactions, fetchBudgets, fetchCategories]);  // Add these functions to the dependency array

    // Handle the submission of the transaction form
    const handleTransactionSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = getAuthToken();
            const formattedDate = new Date().toISOString().split('T')[0];
            const updatedTransaction = { 
                ...transactionForm, 
                transactionDate: formattedDate,
            };
    
            if (transactionForm.id) {
                await axios.put(`http://localhost:8080/api/transactions/${transactionForm.id}`, updatedTransaction, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                toast.success('Transaction updated successfully!', { position: 'top-right' });
            } else {
                await axios.post('http://localhost:8080/api/transactions', updatedTransaction, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                toast.success('Transaction added successfully!', { position: 'top-right' });
            }
            
            setTransactionForm({ description: '', amount: '', date: '', categoryId: '', id: '' });
            fetchTransactions();
        } catch (error) {
            toast.error('Error saving transaction.', { position: 'top-right' });
        }
    };

    // Handle the submission of the budget form
    const handleBudgetSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = getAuthToken();
            const url = budgetForm.id
                ? `http://localhost:8080/api/budgets/${budgetForm.id}`
                : 'http://localhost:8080/api/budgets';
            const method = budgetForm.id ? 'put' : 'post';
            await axios({
                method: method,
                url: url,
                data: budgetForm,
                headers: { Authorization: `Bearer ${token}` },
            });
            toast.success(budgetForm.id ? 'Budget updated successfully!' : 'Budget added successfully!', { position: 'top-right' });
            setBudgetForm({ categoryId: '', amount: '', startDate: '', endDate: '', id: '' });
            fetchBudgets();
        } catch (error) {
            toast.error('Error saving budget.', { position: 'top-right' });
        }
    };

    // Handle delete functionality
    const handleDelete = async () => {
        const token = getAuthToken();
        try {
            if (deleteType === 'transaction') {
                await axios.delete(`http://localhost:8080/api/transactions/${selectedItemId}`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                toast.success('Transaction deleted!', { position: 'top-right' });
                fetchTransactions();
            } else if (deleteType === 'budget') {
                await axios.delete(`http://localhost:8080/api/budgets/${selectedItemId}`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                toast.success('Budget deleted!', { position: 'top-right' });
                fetchBudgets();
            }
        } catch (error) {
            toast.error(`Error deleting ${deleteType}.`, { position: 'top-right' });
        }
        setShowModal(false);
    };

    const openDeleteModal = (id, type) => {
        setSelectedItemId(id);
        setDeleteType(type);
        setShowModal(true);
    };

    const handleDeleteTransaction = (id) => {
        openDeleteModal(id, 'transaction');
    };

    const handleDeleteBudget = (id) => {
        openDeleteModal(id, 'budget');
    };

    const handleEditTransaction = (transaction) => {
        setTransactionForm({
            description: transaction.description,
            amount: transaction.amount,
            date: transaction.transactionDate,
            categoryId: transaction.categoryId,
            id: transaction.id,
        });
        setActiveTab('transactions');
    };

    const handleEditBudget = (budget) => {
        setBudgetForm({
            categoryId: budget.categoryId,
            amount: budget.amount,
            startDate: budget.startDate,
            endDate: budget.endDate,
            id: budget.id,
        });
        setActiveTab('budgets');
    };

    return (
        <div className="container mt-5">
            <Tabs activeKey={activeTab} onSelect={(k) => setActiveTab(k)} className="mb-3">
                <Tab eventKey="transactions" title={<span className="tab-title">Transactions</span>}>
                    <div className="form-container">
                        <h2 className="form-title">Transactions</h2>
                        <Form onSubmit={handleTransactionSubmit}>
                            <Form.Group className="mb-3">
                                <Form.Label>Description</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Enter description"
                                    value={transactionForm.description}
                                    onChange={(e) => setTransactionForm({ ...transactionForm, description: e.target.value })}
                                    required
                                />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Amount</Form.Label>
                                <Form.Control
                                    type="number"
                                    placeholder="Enter amount"
                                    value={transactionForm.amount}
                                    onChange={(e) => setTransactionForm({ ...transactionForm, amount: e.target.value })}
                                    required
                                />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Date</Form.Label>
                                <Form.Control
                                    type="date"
                                    value={transactionForm.date}
                                    onChange={(e) => setTransactionForm({ ...transactionForm, date: e.target.value })}
                                    required
                                />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Category</Form.Label>
                                <Form.Control
                                    as="select"
                                    value={transactionForm.categoryId}
                                    onChange={(e) => setTransactionForm({ ...transactionForm, categoryId: e.target.value })}
                                    required
                                >
                                    <option value="">Select Category</option>
                                    {categories.map((category) => (
                                        <option key={category.id} value={category.id}>
                                            {category.name}
                                        </option>
                                    ))}
                                </Form.Control>
                            </Form.Group>
                            <Button type="submit" variant="primary">{transactionForm.id ? 'Update Transaction' : 'Add Transaction'}</Button>
                        </Form>
                    </div>
                    <Table striped bordered hover className="mt-4">
                        <thead>
                            <tr>
                                <th>Description</th>
                                <th>Amount</th>
                                <th>Date</th>
                                <th>Category</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {transactions.map((transaction) => (
                                <tr key={transaction.id}>
                                    <td>{transaction.description}</td>
                                    <td>{`R ${transaction.amount.toFixed(2)}`}</td>
                                    <td>{transaction.transactionDate}</td>
                                    <td>{transaction.categoryName}</td>
                                    <td>
                                        <div className="d-flex justify-content-center gap-2">
                                            <Button variant="success" size="sm" onClick={() => handleEditTransaction(transaction)}>
                                                Edit
                                            </Button>
                                            <Button variant="danger" size="sm" onClick={() => handleDeleteTransaction(transaction.id)}>
                                                Delete
                                            </Button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                </Tab>
                <Tab eventKey="budgets" title={<span className="tab-title">Budgets</span>}>
                    <div className="form-container">
                        <h2 className="form-title">Budgets</h2>
                        <Form onSubmit={handleBudgetSubmit}>
                            <Form.Group className="mb-3">
                                <Form.Label>Category</Form.Label>
                                <Form.Control
                                    as="select"
                                    value={budgetForm.categoryId}
                                    onChange={(e) => setBudgetForm({ ...budgetForm, categoryId: e.target.value })}
                                    required
                                >
                                    <option value="">Select Category</option>
                                    {categories.map((category) => (
                                        <option key={category.id} value={category.id}>
                                            {category.name}
                                        </option>
                                    ))}
                                </Form.Control>
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Amount</Form.Label>
                                <Form.Control
                                    type="number"
                                    placeholder="Enter amount"
                                    value={budgetForm.amount}
                                    onChange={(e) => setBudgetForm({ ...budgetForm, amount: e.target.value })}
                                    required
                                />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Start Date</Form.Label>
                                <Form.Control
                                    type="date"
                                    value={budgetForm.startDate}
                                    onChange={(e) => setBudgetForm({ ...budgetForm, startDate: e.target.value })}
                                    required
                                />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>End Date</Form.Label>
                                <Form.Control
                                    type="date"
                                    value={budgetForm.endDate}
                                    onChange={(e) => setBudgetForm({ ...budgetForm, endDate: e.target.value })}
                                    required
                                />
                            </Form.Group>
                            <Button type="submit" variant="primary">
                                {budgetForm.id ? 'Update Budget' : 'Add Budget'}
                            </Button>
                        </Form>
                    </div>
                    <Table striped bordered hover className="mt-4">
                        <thead>
                            <tr>
                                <th>Category</th>
                                <th>Amount</th>
                                <th>Start Date</th>
                                <th>End Date</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {budgets.map((budget) => (
                                <tr key={budget.id}>
                                    <td>{budget.categoryName}</td>
                                    <td>{`R ${budget.amount.toFixed(2)}`}</td>
                                    <td>{budget.startDate}</td>
                                    <td>{budget.endDate}</td>
                                    <td>
                                        <div className="d-flex justify-content-center gap-2">
                                            <Button variant="success" size="sm" onClick={() => handleEditBudget(budget)}>
                                                Edit
                                            </Button>
                                            <Button variant="danger" size="sm" onClick={() => handleDeleteBudget(budget.id)}>
                                                Delete
                                            </Button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                </Tab>
            </Tabs>

            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Confirm Deletion</Modal.Title>
                </Modal.Header>
                <Modal.Body>Are you sure you want to delete this {deleteType}?</Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowModal(false)}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={handleDelete}>
                        Delete
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
};

export default TransactionsPage;
