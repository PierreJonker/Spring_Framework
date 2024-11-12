package com.jonkersvault.service;

import com.jonkersvault.dto.BudgetDTO;
import com.jonkersvault.model.Budget;
import com.jonkersvault.model.Category;
import com.jonkersvault.model.User;
import com.jonkersvault.repository.BudgetRepository;
import com.jonkersvault.repository.CategoryRepository;
import com.jonkersvault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Create Budget with the authenticated user's data
    public BudgetDTO createBudget(BudgetDTO budgetDTO) {
        // Get the authenticated user from SecurityContext
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch the category from the database based on categoryId
        Category category = categoryRepository.findById(budgetDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Create a new Budget object and set its properties
        Budget budget = new Budget();
        budget.setUser(user);  // Set user from the authenticated user
        budget.setCategory(category);  // Set category (automatically gets the name from the category)
        budget.setAmount(budgetDTO.getAmount());
        budget.setStartDate(budgetDTO.getStartDate());
        budget.setEndDate(budgetDTO.getEndDate());

        // Save and return the budget
        budget = budgetRepository.save(budget);

        return convertToDTO(budget);  // Convert to BudgetDTO for returning
    }

    // Get all budgets for the authenticated user
    public List<BudgetDTO> getUserBudgets() {
        // Get the authenticated user from SecurityContext
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch all budgets related to the current user
        List<Budget> budgets = budgetRepository.findByUserId(user.getId());

        return budgets.stream()
                .map(this::convertToDTO)  // Convert each Budget to BudgetDTO
                .collect(Collectors.toList());
    }

    // Update an existing Budget
    public BudgetDTO updateBudget(Long budgetId, BudgetDTO budgetDTO) {
        // Get the authenticated user from SecurityContext
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find the existing budget by ID
        Budget existingBudget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        // Fetch the category based on categoryId
        Category category = categoryRepository.findById(budgetDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Update the existing budget details
        existingBudget.setUser(user);
        existingBudget.setCategory(category);
        existingBudget.setAmount(budgetDTO.getAmount());
        existingBudget.setStartDate(budgetDTO.getStartDate());
        existingBudget.setEndDate(budgetDTO.getEndDate());

        // Save and return the updated budget
        existingBudget = budgetRepository.save(existingBudget);

        return convertToDTO(existingBudget);  // Convert to BudgetDTO for returning
    }

    // Delete an existing Budget
    public void deleteBudget(Long budgetId) {
        // Get the authenticated user from SecurityContext
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find the budget by ID
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        // Ensure the user is the owner of the budget
        if (!budget.getUser().equals(user)) {
            throw new RuntimeException("You do not have permission to delete this budget");
        }

        // Delete the budget
        budgetRepository.delete(budget);
    }

    // Convert Budget to BudgetDTO
    private BudgetDTO convertToDTO(Budget budget) {
        BudgetDTO budgetDTO = new BudgetDTO();
        budgetDTO.setId(budget.getId());
        budgetDTO.setCategoryId(budget.getCategory().getId());  // categoryId
        budgetDTO.setAmount(budget.getAmount());
        budgetDTO.setStartDate(budget.getStartDate());
        budgetDTO.setEndDate(budget.getEndDate());

        // Now, also include the category name in the DTO
        budgetDTO.setCategoryName(budget.getCategory().getName());  // categoryName

        return budgetDTO;
    }
}
