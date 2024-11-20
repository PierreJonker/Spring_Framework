package com.jonkersvault.service;

import com.jonkersvault.dto.TransactionDTO;
import com.jonkersvault.model.Transaction;
import com.jonkersvault.model.Category;
import com.jonkersvault.model.User;
import com.jonkersvault.repository.TransactionRepository;
import com.jonkersvault.repository.CategoryRepository;
import com.jonkersvault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public TransactionDTO createTransaction(TransactionDTO transactionDTO, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
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

    public List<TransactionDTO> getUserTransactions(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Transaction> transactions = transactionRepository.findByUserId(user.getId());
        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!existingTransaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only update your own transactions");
        }

        Category category = categoryRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existingTransaction.setCategory(category);
        existingTransaction.setAmount(transactionDTO.getAmount());
        existingTransaction.setTransactionDate(transactionDTO.getTransactionDate());
        existingTransaction.setDescription(transactionDTO.getDescription());

        existingTransaction = transactionRepository.save(existingTransaction);
        return convertToDTO(existingTransaction);
    }

    public void deleteTransaction(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only delete your own transactions");
        }

        transactionRepository.delete(transaction);
    }

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