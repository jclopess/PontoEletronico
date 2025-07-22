package br.gov.pr.lottopar.registroponto.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "justifications")
@Data
public class Justificativa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("usuario-justificativas")
    private Usuario user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String type; // "absence", "late", "early-leave", "error"

    @Column(nullable = false, length = 500)
    private String reason;

    @Column(nullable = false)
    private String status; // "pending", "approved", "rejected"

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Usuario approvedBy;

    private LocalDateTime approvedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}