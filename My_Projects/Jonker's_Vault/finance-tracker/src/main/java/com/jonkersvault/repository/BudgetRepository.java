package com.jonkersvault.repository;

import com.jonkersvault.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUserId(Long userId);
}
