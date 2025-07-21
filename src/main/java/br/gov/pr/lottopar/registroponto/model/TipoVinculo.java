package br.gov.pr.lottopar.registroponto.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "employment_types")
@Data
public class TipoVinculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // Ex: "CLT", "Estagiário"

    private String description;

    @Column(precision = 4, scale = 2, nullable = false)
    private BigDecimal dailyWorkHours; // Ex: 8.00, 6.00

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}