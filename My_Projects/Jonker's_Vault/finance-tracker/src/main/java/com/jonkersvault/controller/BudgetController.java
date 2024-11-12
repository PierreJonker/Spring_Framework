package com.jonkersvault.controller;

import com.jonkersvault.dto.BudgetDTO;
import com.jonkersvault.model.Budget;
import com.jonkersvault.service.BudgetService;
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

    // Create Budget
    @PostMapping
    public ResponseEntity<BudgetDTO> createBudget(@RequestBody BudgetDTO budgetDTO) {
        BudgetDTO createdBudget = budgetService.createBudget(budgetDTO);
        return new ResponseEntity<>(createdBudget, HttpStatus.CREATED);
    }

    // Get all budgets for the authenticated user
    @GetMapping
    public ResponseEntity<List<BudgetDTO>> getUserBudgets() {
        List<BudgetDTO> budgets = budgetService.getUserBudgets();
        return new ResponseEntity<>(budgets, HttpStatus.OK);
    }

    // Update an existing Budget
    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetDTO> updateBudget(@PathVariable Long budgetId, @RequestBody BudgetDTO budgetDTO) {
        BudgetDTO updatedBudget = budgetService.updateBudget(budgetId, budgetDTO);
        return new ResponseEntity<>(updatedBudget, HttpStatus.OK);
    }

    // Delete an existing Budget
    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long budgetId) {
        budgetService.deleteBudget(budgetId);
        return ResponseEntity.noContent().build();
    }
}
