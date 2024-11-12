package com.jonkersvault.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "budgets")  // Explicit table name
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Many budgets can belong to one user
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) // Many budgets can belong to one category
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private BigDecimal amount; // The budget amount

    private LocalDate startDate; // Budget start date

    private LocalDate endDate; // Budget end date

    private LocalDate createdAt; // Created timestamp

    @PrePersist
    public void onCreate() {
        createdAt = LocalDate.now(); // Automatically set the creation date
    }
}
