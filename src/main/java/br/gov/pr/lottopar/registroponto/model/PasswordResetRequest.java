package br.gov.pr.lottopar.registroponto.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_requests")
@Data
public class PasswordResetRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private LocalDateTime requestedAt = LocalDateTime.now();

    @Column(nullable = false)
    private String status; // "pending", "resolved"

    @ManyToOne
    @JoinColumn(name = "resolved_by")
    private Usuario resolvedBy;

    private LocalDateTime resolvedAt;
}