package com.jonkersvault.service;

import com.jonkersvault.dto.FinancialGoalDTO;
import com.jonkersvault.model.FinancialGoal;
import com.jonkersvault.model.User;
import com.jonkersvault.repository.FinancialGoalRepository;
import com.jonkersvault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialGoalService {

    @Autowired
    private FinancialGoalRepository financialGoalRepository;

    @Autowired
    private UserRepository userRepository;

    public FinancialGoalDTO createGoal(FinancialGoalDTO goalDTO, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FinancialGoal goal = new FinancialGoal();
        goal.setUser(user);
        goal.setGoalName(goalDTO.getGoalName());
        goal.setTargetAmount(goalDTO.getTargetAmount());
        goal.setCurrentAmount(goalDTO.getCurrentAmount());
        goal.setTargetDate(goalDTO.getTargetDate());

        goal = financialGoalRepository.save(goal);
        return convertToDTO(goal);
    }

    public List<FinancialGoalDTO> getGoalsByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<FinancialGoal> goals = financialGoalRepository.findByUser(user);
        return goals.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FinancialGoalDTO updateGoal(Long goalId, FinancialGoalDTO goalDTO, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FinancialGoal existingGoal = financialGoalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        if (!existingGoal.getUser().equals(user)) {
            throw new RuntimeException("You can only update your own goals");
        }

        existingGoal.setGoalName(goalDTO.getGoalName());
        existingGoal.setTargetAmount(goalDTO.getTargetAmount());
        existingGoal.setCurrentAmount(goalDTO.getCurrentAmount());
        existingGoal.setTargetDate(goalDTO.getTargetDate());

        existingGoal = financialGoalRepository.save(existingGoal);
        return convertToDTO(existingGoal);
    }

    public void deleteGoal(Long goalId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FinancialGoal goal = financialGoalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        if (!goal.getUser().equals(user)) {
            throw new RuntimeException("You can only delete your own goals");
        }

        financialGoalRepository.delete(goal);
    }

    private FinancialGoalDTO convertToDTO(FinancialGoal goal) {
        FinancialGoalDTO dto = new FinancialGoalDTO();
        dto.setId(goal.getId());
        dto.setGoalName(goal.getGoalName());
        dto.setTargetAmount(goal.getTargetAmount());
        dto.setCurrentAmount(goal.getCurrentAmount());
        dto.setTargetDate(goal.getTargetDate());
        dto.setCreatedAt(goal.getCreatedAt());
        return dto;
    }
}