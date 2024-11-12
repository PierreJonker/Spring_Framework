package com.jonkersvault.controller;

import com.jonkersvault.dto.FinancialGoalDTO;
import com.jonkersvault.model.FinancialGoal;
import com.jonkersvault.service.FinancialGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class FinancialGoalsController {

    @Autowired
    private FinancialGoalService financialGoalService;

    // Create a financial goal
    @PostMapping
    public FinancialGoalDTO createGoal(@RequestBody FinancialGoalDTO financialGoalDTO) {
        FinancialGoal financialGoal = new FinancialGoal();
        financialGoal.setGoalName(financialGoalDTO.getGoalName());
        financialGoal.setTargetAmount(financialGoalDTO.getTargetAmount());
        financialGoal.setCurrentAmount(financialGoalDTO.getCurrentAmount());
        financialGoal.setTargetDate(financialGoalDTO.getTargetDate());

        FinancialGoal createdGoal = financialGoalService.createGoal(financialGoal);
        return mapToDTO(createdGoal);
    }

    // Get all financial goals for the logged-in user
    @GetMapping
    public List<FinancialGoalDTO> getGoals() {
        List<FinancialGoal> goals = financialGoalService.getGoalsByUser();
        return goals.stream().map(this::mapToDTO).toList();
    }

    // Update a financial goal for the logged-in user
    @PutMapping("/{goalId}")
    public FinancialGoalDTO updateGoal(@PathVariable Long goalId, @RequestBody FinancialGoalDTO financialGoalDTO) {
        FinancialGoal updatedGoal = financialGoalService.updateGoal(goalId, mapToEntity(financialGoalDTO));
        return mapToDTO(updatedGoal);
    }

    // Delete a financial goal for the logged-in user
    @DeleteMapping("/{goalId}")
    public void deleteGoal(@PathVariable Long goalId) {
        financialGoalService.deleteGoal(goalId);
    }

    // Map FinancialGoal to FinancialGoalDTO
    private FinancialGoalDTO mapToDTO(FinancialGoal financialGoal) {
        FinancialGoalDTO dto = new FinancialGoalDTO();
        dto.setId(financialGoal.getId());
        dto.setGoalName(financialGoal.getGoalName());
        dto.setTargetAmount(financialGoal.getTargetAmount());
        dto.setCurrentAmount(financialGoal.getCurrentAmount());
        dto.setTargetDate(financialGoal.getTargetDate());
        dto.setCreatedAt(financialGoal.getCreatedAt());
        return dto;
    }

    // Map FinancialGoalDTO to FinancialGoal entity
    private FinancialGoal mapToEntity(FinancialGoalDTO financialGoalDTO) {
        FinancialGoal financialGoal = new FinancialGoal();
        financialGoal.setGoalName(financialGoalDTO.getGoalName());
        financialGoal.setTargetAmount(financialGoalDTO.getTargetAmount());
        financialGoal.setCurrentAmount(financialGoalDTO.getCurrentAmount());
        financialGoal.setTargetDate(financialGoalDTO.getTargetDate());
        return financialGoal;
    }
}
