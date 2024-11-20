package com.jonkersvault.service;

import com.jonkersvault.dto.BudgetDTO;
import com.jonkersvault.model.Budget;
import com.jonkersvault.model.Category;
import com.jonkersvault.model.User;
import com.jonkersvault.repository.BudgetRepository;
import com.jonkersvault.repository.CategoryRepository;
import com.jonkersvault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public BudgetDTO createBudget(BudgetDTO budgetDTO, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(budgetDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Budget budget = new Budget();
        budget.setUser(user);
        budget.setCategory(category);
        budget.setAmount(budgetDTO.getAmount());
        budget.setStartDate(budgetDTO.getStartDate());
        budget.setEndDate(budgetDTO.getEndDate());

        budget = budgetRepository.save(budget);
        return convertToDTO(budget);
    }

    public List<BudgetDTO> getUserBudgets(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Budget> budgets = budgetRepository.findByUserId(user.getId());
        return budgets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BudgetDTO updateBudget(Long budgetId, BudgetDTO budgetDTO, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget existingBudget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!existingBudget.getUser().equals(user)) {
            throw new RuntimeException("You can only update your own budgets");
        }

        Category category = categoryRepository.findById(budgetDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existingBudget.setCategory(category);
        existingBudget.setAmount(budgetDTO.getAmount());
        existingBudget.setStartDate(budgetDTO.getStartDate());
        existingBudget.setEndDate(budgetDTO.getEndDate());

        existingBudget = budgetRepository.save(existingBudget);
        return convertToDTO(existingBudget);
    }

    public void deleteBudget(Long budgetId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!budget.getUser().equals(user)) {
            throw new RuntimeException("You do not have permission to delete this budget");
        }

        budgetRepository.delete(budget);
    }

    private BudgetDTO convertToDTO(Budget budget) {
        BudgetDTO budgetDTO = new BudgetDTO();
        budgetDTO.setId(budget.getId());
        budgetDTO.setUserId(budget.getUser().getId());
        budgetDTO.setCategoryId(budget.getCategory().getId());
        budgetDTO.setCategoryName(budget.getCategory().getName());
        budgetDTO.setAmount(budget.getAmount());
        budgetDTO.setStartDate(budget.getStartDate());
        budgetDTO.setEndDate(budget.getEndDate());
        budgetDTO.setCreatedAt(budget.getCreatedAt());
        return budgetDTO;
    }
}