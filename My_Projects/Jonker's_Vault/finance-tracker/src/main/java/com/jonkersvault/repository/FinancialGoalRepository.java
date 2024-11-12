package com.jonkersvault.repository;

import com.jonkersvault.model.FinancialGoal;
import com.jonkersvault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Long> {
    List<FinancialGoal> findByUser(User user);
}
