// src/components/TransactionsPage.js
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
    const [transactionForm, setTransactionForm] = useState({ description: '', amount: '', date: '' });
    const [budgetForm, setBudgetForm] = useState({ name: '', amount: '' });

    // Fetch data on component load
    useEffect(() => {
        fetchTransactions();
        fetchBudgets();
    }, []);

    const fetchTransactions = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/transactions');
            setTransactions(response.data);
        } catch (error) {
            toast.error('Error fetching transactions.', { position: 'top-right' });
        }
    };

    const fetchBudgets = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/budgets');
            setBudgets(response.data);
        } catch (error) {
            toast.error('Error fetching budgets.', { position: 'top-right' });
        }
    };

    const handleTransactionSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/transactions', transactionForm);
            toast.success('Transaction added successfully!', { position: 'top-right' });
            setTransactionForm({ description: '', amount: '', date: '' });
            fetchTransactions(); // Refresh transactions
        } catch (error) {
            toast.error('Error adding transaction.', { position: 'top-right' });
        }
    };

    const handleBudgetSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/budgets', budgetForm);
            toast.success('Budget added successfully!', { position: 'top-right' });
            setBudgetForm({ name: '', amount: '' });
            fetchBudgets(); // Refresh budgets
        } catch (error) {
            toast.error('Error adding budget.', { position: 'top-right' });
        }
    };

    const handleDeleteTransaction = async (id) => {
        if (!window.confirm('Are you sure you want to delete this transaction?')) return;
        try {
            await axios.delete(`http://localhost:8080/api/transactions/${id}`);
            toast.success('Transaction deleted!', { position: 'top-right' });
            fetchTransactions(); // Refresh transactions
        } catch (error) {
            toast.error('Error deleting transaction.', { position: 'top-right' });
        }
    };

    const handleDeleteBudget = async (id) => {
        if (!window.confirm('Are you sure you want to delete this budget?')) return;
        try {
            await axios.delete(`http://localhost:8080/api/budgets/${id}`);
            toast.success('Budget deleted!', { position: 'top-right' });
            fetchBudgets(); // Refresh budgets
        } catch (error) {
            toast.error('Error deleting budget.', { position: 'top-right' });
        }
    };

    return (
        <div className="container mt-5">
            <Tabs
                activeKey={activeTab}
                onSelect={(k) => setActiveTab(k)}
                className="mb-3"
            >
                <Tab eventKey="transactions" title="Transactions">
                    <h2>Transactions</h2>
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
                        <Button type="submit" variant="primary">Add Transaction</Button>
                    </Form>
                    <Table striped bordered hover className="mt-4">
                        <thead>
                            <tr>
                                <th>Description</th>
                                <th>Amount</th>
                                <th>Date</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {transactions.map((transaction) => (
                                <tr key={transaction.id}>
                                    <td>{transaction.description}</td>
                                    <td>{transaction.amount}</td>
                                    <td>{transaction.date}</td>
                                    <td>
                                        <Button variant="danger" size="sm" onClick={() => handleDeleteTransaction(transaction.id)}>
                                            Delete
                                        </Button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                </Tab>
                <Tab eventKey="budgets" title="Budgets">
                    <h2>Budgets</h2>
                    <Form onSubmit={handleBudgetSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Name</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Enter budget name"
                                value={budgetForm.name}
                                onChange={(e) => setBudgetForm({ ...budgetForm, name: e.target.value })}
                                required
                            />
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
                        <Button type="submit" variant="primary">Add Budget</Button>
                    </Form>
                    <Table striped bordered hover className="mt-4">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Amount</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {budgets.map((budget) => (
                                <tr key={budget.id}>
                                    <td>{budget.name}</td>
                                    <td>{budget.amount}</td>
                                    <td>
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
