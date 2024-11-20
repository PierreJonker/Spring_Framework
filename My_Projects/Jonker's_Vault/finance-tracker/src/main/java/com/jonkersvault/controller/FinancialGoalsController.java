package com.jonkersvault.controller;

import com.jonkersvault.dto.FinancialGoalDTO;
import com.jonkersvault.service.FinancialGoalService;
import com.jonkersvault.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class FinancialGoalsController {

    @Autowired
    private FinancialGoalService financialGoalService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<FinancialGoalDTO> createGoal(@RequestBody FinancialGoalDTO goalDTO,
                                                       @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        FinancialGoalDTO createdGoal = financialGoalService.createGoal(goalDTO, email);
        return new ResponseEntity<>(createdGoal, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FinancialGoalDTO>> getGoals(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        List<FinancialGoalDTO> goals = financialGoalService.getGoalsByUser(email);
        return new ResponseEntity<>(goals, HttpStatus.OK);
    }

    @PutMapping("/{goalId}")
    public ResponseEntity<FinancialGoalDTO> updateGoal(@PathVariable Long goalId,
                                                       @RequestBody FinancialGoalDTO goalDTO,
                                                       @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        FinancialGoalDTO updatedGoal = financialGoalService.updateGoal(goalId, goalDTO, email);
        return new ResponseEntity<>(updatedGoal, HttpStatus.OK);
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long goalId,
                                           @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        financialGoalService.deleteGoal(goalId, email);
        return ResponseEntity.noContent().build();
    }
}