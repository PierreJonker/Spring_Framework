import React, { useState, useEffect } from 'react';
import { Form, Button, Table, Tabs, Tab } from 'react-bootstrap';
import axios from 'axios';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import '../App.css';

const TransactionsPage = () => {
    const [activeTab, setActiveTab] = useState('transactions');
    const [transactions, setTransactions] = useState([]);
    const [budgets, setBudgets] = useState([]);
    const [categories, setCategories] = useState([]);
    const [transactionForm, setTransactionForm] = useState({ description: '', amount: '', date: '', categoryId: '' });
    const [budgetForm, setBudgetForm] = useState({ categoryId: '', amount: '', startDate: '', endDate: '', id: '' });

    useEffect(() => {
        fetchTransactions();
        fetchBudgets();
        fetchCategories();
    }, []);

    const getAuthToken = () => {
        return localStorage.getItem('token');
    };

    const fetchTransactions = async () => {
        try {
            const token = getAuthToken();
            const response = await axios.get('http://localhost:8080/api/transactions', {
                headers: { Authorization: `Bearer ${token}` },
            });
            setTransactions(response.data);
        } catch (error) {
            toast.error('Error fetching transactions.', { position: 'top-right' });
        }
    };

    const fetchBudgets = async () => {
        try {
            const token = getAuthToken();
            const response = await axios.get('http://localhost:8080/api/budgets', {
                headers: { Authorization: `Bearer ${token}` },
            });
            setBudgets(response.data);
        } catch (error) {
            toast.error('Error fetching budgets.', { position: 'top-right' });
        }
    };

    const fetchCategories = async () => {
        try {
            const token = getAuthToken();
            const response = await axios.get('http://localhost:8080/api/categories', {
                headers: { Authorization: `Bearer ${token}` },
            });
            setCategories(response.data);
        } catch (error) {
            toast.error('Error fetching categories.', { position: 'top-right' });
        }
    };

    const handleTransactionSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = getAuthToken();
            await axios.post('http://localhost:8080/api/transactions', transactionForm, {
                headers: { Authorization: `Bearer ${token}` },
            });
            toast.success('Transaction added successfully!', { position: 'top-right' });
            setTransactionForm({ description: '', amount: '', date: '', categoryId: '' });
            fetchTransactions();
        } catch (error) {
            toast.error('Error adding transaction.', { position: 'top-right' });
        }
    };

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

    const handleDeleteTransaction = async (id) => {
        if (!window.confirm('Are you sure you want to delete this transaction?')) return;
        try {
            const token = getAuthToken();
            await axios.delete(`http://localhost:8080/api/transactions/${id}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            toast.success('Transaction deleted!', { position: 'top-right' });
            fetchTransactions();
        } catch (error) {
            toast.error('Error deleting transaction.', { position: 'top-right' });
        }
    };

    const handleDeleteBudget = async (id) => {
        if (!window.confirm('Are you sure you want to delete this budget?')) return;
        try {
            const token = getAuthToken();
            await axios.delete(`http://localhost:8080/api/budgets/${id}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            toast.success('Budget deleted!', { position: 'top-right' });
            fetchBudgets();
        } catch (error) {
            toast.error('Error deleting budget.', { position: 'top-right' });
        }
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
                            <Button type="submit" variant="primary">Add Transaction</Button>
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
                                    <td>{transaction.date}</td>
                                    <td>{transaction.categoryName}</td>
                                    <td style={{ textAlign: 'center' }}>
                                        <Button variant="danger" size="sm" onClick={() => handleDeleteTransaction(transaction.id)}>
                                            Delete
                                        </Button>
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
                                    <td style={{ textAlign: 'center' }}>
                                        <Button variant="success" size="sm" onClick={() => handleEditBudget(budget)}>
                                            Edit
                                        </Button>
                                        <Button variant="danger" size="sm" onClick={() => handleDeleteBudget(budget.id)}>
                                            Delete
                                        </Button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                </Tab>
            </Tabs>
        </div>
    );
};

export default TransactionsPage;
