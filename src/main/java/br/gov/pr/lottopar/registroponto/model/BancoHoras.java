package br.gov.pr.lottopar.registroponto.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "hour_bank")
@Data
public class BancoHoras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("usuario-bancohoras")
    private Usuario user;

    @Column(nullable = false)
    private String month; // Formato "YYYY-MM"

    @Column(precision = 6, scale = 2, nullable = false)
    private BigDecimal expectedHours;

    @Column(precision = 6, scale = 2, nullable = false)
    private BigDecimal workedHours;

    @Column(precision = 6, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();
}