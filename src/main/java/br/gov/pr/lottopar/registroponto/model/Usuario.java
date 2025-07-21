package br.gov.pr.lottopar.registroponto.model;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto; // Ex: "JACKSON LOPES ROCHA DA SILVA" [cite: 44]
    @Column(unique = true)
    private String login; // Campo usado para login na página web [cite: 28]
    private String senha; // Será armazenada de forma criptografada [cite: 28, 62]
    private Long codigo; // Ex: "Código: 24" [cite: 56]
    private LocalDate dataCadastro; // Ex: "27/02/2023" [cite: 56]
    @Enumerated(EnumType.STRING)
    private Perfil perfil; // Define se é FUNCIONARIO, GESTOR ou ADMIN
    private boolean senhaTemporariaAtiva; // Controla a obrigatoriedade de trocar a senha [cite: 32]

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @ManyToOne
    @JoinColumn(name = "funcao_id")
    private Funcao funcao;

    @ManyToOne
    @JoinColumn(name = "carga_horaria_id")
    private CargaHoraria cargaHoraria;

    @ManyToOne
    @JoinColumn(name = "gestor_id")
    private Usuario gestor; // Representa a chefia imediata [cite: 55]

    @OneToMany(mappedBy = "usuario")
    private List<RegistroPonto> registrosPonto;
}