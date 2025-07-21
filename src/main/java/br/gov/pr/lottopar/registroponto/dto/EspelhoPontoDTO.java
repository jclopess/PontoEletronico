package br.gov.pr.lottopar.registroponto.dto;

import lombok.Data;
import java.util.List;

@Data
public class EspelhoPontoDTO {
    // Cabeçalho
    private String nomeFuncionario; // [cite: 44]
    private String departamento; // [cite: 45]
    private String mesReferencia; // Ex: "Julho/2025" [cite: 46]
    private String dataCadastroFuncionario; // Ex: "Funcionário Cadastrado... 27/02/2023 Código: 24" [cite: 56]
    private String nomeGestor; // [cite: 55]

    // Corpo do Relatório
    private List<RegistroDiarioDTO> registrosDiarios;

    // Rodapé
    private TotalizacaoDTO totalizacao;
    private String relatorioGeradoPor; // Ex: "jackson.s 10.3.2.116... 17/07/2025 11:44" [cite: 54]
}