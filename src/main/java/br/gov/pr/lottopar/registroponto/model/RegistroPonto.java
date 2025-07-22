package br.gov.pr.lottopar.registroponto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor; // Importar a anotação do Lombok

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "time_records")
@Data
@NoArgsConstructor // <-- CORREÇÃO: Adiciona o construtor vazio que o JPA precisa
public class RegistroPonto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("usuario-registros")
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDate date;

    private LocalTime entry1;
    private LocalTime exit1;
    private LocalTime entry2;
    private LocalTime exit2;

    @Column(precision = 4, scale = 2)
    private BigDecimal totalHours;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    // Este construtor continua útil para o PontoService
    public RegistroPonto(Usuario usuario, LocalDate date) {
        this.usuario = usuario;
        this.date = date;
    }
}