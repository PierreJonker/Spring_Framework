package com.jonkersvault.service;

import com.jonkersvault.dto.TransactionDTO;
import com.jonkersvault.model.Transaction;
import com.jonkersvault.model.Category;
import com.jonkersvault.model.User;
import com.jonkersvault.repository.TransactionRepository;
import com.jonkersvault.repository.CategoryRepository;
import com.jonkersvault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Create Transaction with the authenticated user's data
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTransactionDate(transactionDTO.getTransactionDate());
        transaction.setDescription(transactionDTO.getDescription());

        transaction = transactionRepository.save(transaction);
        return convertToDTO(transaction);
    }

    // Get all transactions for the authenticated user
    public List<TransactionDTO> getUserTransactions() {
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Transaction> transactions = transactionRepository.findByUserId(user.getId());
        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Update an existing transaction
    public TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO) {
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find the transaction to update
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Update the transaction fields
        Category category = categoryRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existingTransaction.setUser(user);
        existingTransaction.setCategory(category);
        existingTransaction.setAmount(transactionDTO.getAmount());
        existingTransaction.setTransactionDate(transactionDTO.getTransactionDate());
        existingTransaction.setDescription(transactionDTO.getDescription());

        // Save the updated transaction
        existingTransaction = transactionRepository.save(existingTransaction);

        return convertToDTO(existingTransaction); // Return the updated transaction as DTO
    }

    // Delete an existing transaction
    public void deleteTransaction(Long id) {
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find the transaction by ID
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Ensure the user is the owner of the transaction
        if (!transaction.getUser().equals(user)) {
            throw new RuntimeException("You do not have permission to delete this transaction");
        }

        // Delete the transaction
        transactionRepository.delete(transaction);
    }

    // Convert Transaction to TransactionDTO
    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setCategoryId(transaction.getCategory().getId());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setTransactionDate(transaction.getTransactionDate());
        transactionDTO.setDescription(transaction.getDescription());
        transactionDTO.setCreatedAt(transaction.getCreatedAt());
        return transactionDTO;
    }
}
