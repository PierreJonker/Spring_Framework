package com.jonkersvault.repository;

import com.jonkersvault.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    // Custom query to find all budgets by userId
    List<Budget> findByUserId(Long userId);
}
