package com.jonkersvault.model;

import lombok.Data;
import jakarta.persistence.*;  // Using jakarta.persistence
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "financial_goals") // Explicitly name the table to ensure it matches your DB schema
public class FinancialGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many goals can belong to one user, hence ManyToOne relationship
    @ManyToOne(fetch = FetchType.LAZY)  // Use LAZY fetch to avoid unnecessary loading of user
    @JoinColumn(name = "user_id", nullable = false) // Foreign key referencing the user table
    private User user; // Associated user

    @Column(nullable = false)
    private String goalName;

    @Column(nullable = false)
    private BigDecimal targetAmount;

    private BigDecimal currentAmount;

    private LocalDate targetDate;

    private LocalDate createdAt;

    // Automatically set the creation date before persisting the entity
    @PrePersist
    public void onCreate() {
        createdAt = LocalDate.now();
    }

    // Optionally, you can add custom methods to format or manipulate the data as needed.
}
