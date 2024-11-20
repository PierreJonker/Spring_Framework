package com.jonkersvault.controller;

import com.jonkersvault.dto.BudgetDTO;
import com.jonkersvault.service.BudgetService;
import com.jonkersvault.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private JwtUtil jwtUtil;

    // Create Budget
    @PostMapping
    public ResponseEntity<BudgetDTO> createBudget(@RequestBody BudgetDTO budgetDTO, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));  // Extract email from token (bearer token)
        BudgetDTO createdBudget = budgetService.createBudget(budgetDTO, email);
        return new ResponseEntity<>(createdBudget, HttpStatus.CREATED);
    }

    // Get all budgets for the authenticated user
    @GetMapping
    public ResponseEntity<List<BudgetDTO>> getUserBudgets(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));  // Extract email from token (bearer token)
        List<BudgetDTO> budgets = budgetService.getUserBudgets(email);
        return new ResponseEntity<>(budgets, HttpStatus.OK);
    }

    // Update an existing Budget
    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetDTO> updateBudget(@PathVariable Long budgetId, @RequestBody BudgetDTO budgetDTO, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));  // Extract email from token (bearer token)
        BudgetDTO updatedBudget = budgetService.updateBudget(budgetId, budgetDTO, email);
        return new ResponseEntity<>(updatedBudget, HttpStatus.OK);
    }

    // Delete an existing Budget
    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long budgetId, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));  // Extract email from token (bearer token)
        budgetService.deleteBudget(budgetId, email);
        return ResponseEntity.noContent().build();
    }
}
