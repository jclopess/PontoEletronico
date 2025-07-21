package br.gov.pr.lottopar.registroponto.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "users") // Usando "users" para manter consistência com o schema anterior
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cpf; // Campo principal para login, deve ser único

    @Column(unique = true, nullable = false)
    private String username; // Pode ser o mesmo que o CPF ou um nome de usuário

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name; // Nome completo

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Perfil role; // 'EMPLOYEE', 'MANAGER', 'ADMIN'

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Departamento department;

    @ManyToOne
    @JoinColumn(name = "function_id")
    private Funcao function;

    @ManyToOne
    @JoinColumn(name = "employment_type_id")
    private TipoVinculo employmentType; // Renomeado de CargaHoraria

    private LocalDate admissionDate;
    private LocalDate dismissalDate;

    @Column(nullable = false)
    private String status; // "active", "blocked", "inactive"

    @Column(precision = 4, scale = 2, nullable = false)
    private BigDecimal dailyWorkHours;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt = LocalDate.now();

    // Relações
    @OneToMany(mappedBy = "usuario")
    private List<RegistroPonto> registrosPonto;

    @OneToMany(mappedBy = "user")
    private List<Justificativa> justificativas;

    @OneToMany(mappedBy = "user")
    private List<BancoHoras> bancoHoras;
}