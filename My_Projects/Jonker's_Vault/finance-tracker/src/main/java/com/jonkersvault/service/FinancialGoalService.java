package com.jonkersvault.service;

import com.jonkersvault.model.FinancialGoal;
import com.jonkersvault.repository.FinancialGoalRepository;
import com.jonkersvault.repository.UserRepository;  // Import the UserRepository
import com.jonkersvault.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialGoalService {

    @Autowired
    private FinancialGoalRepository financialGoalRepository;

    @Autowired
    private UserRepository userRepository;  // Corrected the injection here

    // Create a financial goal for the logged-in user
    public FinancialGoal createGoal(FinancialGoal financialGoal) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        financialGoal.setUser(user); // Associate the goal with the current user
        return financialGoalRepository.save(financialGoal);
    }

    // Get all financial goals for the logged-in user
    public List<FinancialGoal> getGoalsByUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return financialGoalRepository.findByUser(user); // Fetch only goals for the logged-in user
    }

    // Update a financial goal for the logged-in user
    public FinancialGoal updateGoal(Long goalId, FinancialGoal updatedGoal) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FinancialGoal existingGoal = financialGoalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        if (!existingGoal.getUser().equals(user)) {
            throw new RuntimeException("You can only update your own goals");
        }

        existingGoal.setGoalName(updatedGoal.getGoalName());
        existingGoal.setTargetAmount(updatedGoal.getTargetAmount());
        existingGoal.setCurrentAmount(updatedGoal.getCurrentAmount());
        existingGoal.setTargetDate(updatedGoal.getTargetDate());
        return financialGoalRepository.save(existingGoal);
    }

    // Delete a financial goal for the logged-in user
    public void deleteGoal(Long goalId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FinancialGoal existingGoal = financialGoalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        if (!existingGoal.getUser().equals(user)) {
            throw new RuntimeException("You can only delete your own goals");
        }

        financialGoalRepository.delete(existingGoal);
    }
}
