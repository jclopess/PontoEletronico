package br.gov.pr.lottopar.registroponto.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Getter // Use Getter em vez de @Data
@Setter // Use Setter em vez de @Data
@EqualsAndHashCode(exclude = {"registrosPonto", "justificativas", "bancoHoras", "gestor"}) // Exclui campos de relacionamento
@ToString(exclude = {"registrosPonto", "justificativas", "bancoHoras", "gestor"}) // Exclui campos de relacionamento para evitar loops
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Perfil role;

    @ManyToOne(fetch = FetchType.LAZY) // Usar LAZY é uma boa prática
    @JoinColumn(name = "department_id")
    private Departamento department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "function_id")
    private Funcao function;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employment_type_id")
    private TipoVinculo employmentType;

    private LocalDate admissionDate;
    private LocalDate dismissalDate;

    @Column(nullable = false)
    private String status;

    @Column(precision = 4, scale = 2, nullable = false)
    private BigDecimal dailyWorkHours;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt = LocalDate.now();

    // --- Relacionamento de auto-referência ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gestor_id")
    private Usuario gestor;

    // --- Relacionamentos com outras tabelas ---
    @OneToMany(mappedBy = "usuario")
    private List<RegistroPonto> registrosPonto;

    @OneToMany(mappedBy = "user")
    private List<Justificativa> justificativas;

    @OneToMany(mappedBy = "user")
    private List<BancoHoras> bancoHoras;
}