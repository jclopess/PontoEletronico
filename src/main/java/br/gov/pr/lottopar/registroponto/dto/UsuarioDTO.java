package br.gov.pr.lottopar.registroponto.dto;

import br.gov.pr.lottopar.registroponto.model.Perfil;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UsuarioDTO {
    // Dados Pessoais
    private String name;
    private String cpf;
    private String username;
    private String phone;
    private String password; // Apenas para criação ou alteração

    // Dados Organizacionais
    private Perfil role;
    private Long departmentId;
    private Long functionId;
    private Long employmentTypeId;
    private Long gestorId;

    // Dados de Contrato
    private LocalDate admissionDate;
    private String status;
}