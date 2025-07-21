package br.gov.pr.lottopar.registroponto.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RegistroPonto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private LocalDateTime dataHora; // Armazena a data e o horário exato da marcação [cite: 4]
    private String localizacao; // Localização da marcação [cite: 5]
    @Column(length = 500)
    private String justificativa; // Campo para justificativa de ausências ou erros [cite: 39]
    private boolean justificado; // Indica se a marcação ou ausência foi justificada [cite: 41]
    private boolean aguardandoAnalise; // Indica pendência de análise da chefia [cite: 49, 52]
}