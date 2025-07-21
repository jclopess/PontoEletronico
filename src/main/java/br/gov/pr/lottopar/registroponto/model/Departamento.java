package br.gov.pr.lottopar.registroponto.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome; // Ex: "GS - ASSESSORIA TÉCNICA DE INFORMÁTICA E COMUNICAÇÃO" [cite: 22, 45]
}