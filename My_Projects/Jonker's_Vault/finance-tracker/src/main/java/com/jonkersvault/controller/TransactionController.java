package com.jonkersvault.controller;

import com.jonkersvault.dto.TransactionDTO;
import com.jonkersvault.service.TransactionService;
import com.jonkersvault.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private JwtUtil jwtUtil;

    // Create transaction
    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));  // Extract email from token (bearer token)
        TransactionDTO createdTransaction = transactionService.createTransaction(transactionDTO, email);
        return ResponseEntity.ok(createdTransaction);
    }

    // Get all transactions for the authenticated user
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getUserTransactions(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));  // Extract email from token (bearer token)
        List<TransactionDTO> transactions = transactionService.getUserTransactions(email);
        return ResponseEntity.ok(transactions);
    }

    // Update an existing transaction
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long id, @RequestBody TransactionDTO transactionDTO, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));  // Extract email from token (bearer token)
        TransactionDTO updatedTransaction = transactionService.updateTransaction(id, transactionDTO, email);
        return ResponseEntity.ok(updatedTransaction);
    }

    // Delete an existing transaction
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));  // Extract email from token (bearer token)
        transactionService.deleteTransaction(id, email);
        return ResponseEntity.noContent().build();
    }
}
