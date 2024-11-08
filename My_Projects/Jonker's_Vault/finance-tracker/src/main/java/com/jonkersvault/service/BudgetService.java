package com.jonkersvault.service;

import com.jonkersvault.model.Budget;
import com.jonkersvault.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {
    @Autowired
    private BudgetRepository budgetRepository;

    public List<Budget> getBudgetsByUserId(Long userId) {
        return budgetRepository.findByUserId(userId);
    }

    public Budget saveBudget(Budget budget) {
        return budgetRepository.save(budget);
    }
}
