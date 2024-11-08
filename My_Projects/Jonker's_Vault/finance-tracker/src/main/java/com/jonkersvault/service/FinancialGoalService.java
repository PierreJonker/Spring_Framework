package com.jonkersvault.service;

import com.jonkersvault.model.FinancialGoal;
import com.jonkersvault.repository.FinancialGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialGoalService {
    @Autowired
    private FinancialGoalRepository financialGoalRepository;

    public List<FinancialGoal> getGoalsByUserId(Long userId) {
        return financialGoalRepository.findByUserId(userId);
    }

    public FinancialGoal saveGoal(FinancialGoal goal) {
        return financialGoalRepository.save(goal);
    }
}
