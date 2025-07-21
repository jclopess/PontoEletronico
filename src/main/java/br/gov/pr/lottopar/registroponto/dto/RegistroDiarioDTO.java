package br.gov.pr.lottopar.registroponto.dto;

import lombok.Data;
import java.util.List;

@Data
public class RegistroDiarioDTO {
    private String dia; // Ex: "01/07 - terça" [cite: 37]
    private List<String> marcacoes; // Ex: ["09:23", "12:22", "13:22", "17:07"] [cite: 38]
    private String totalHorasTrabalhadasDia; // Ex: "06:44" [cite: 40]
    private String resultadoDia; // Ex: "horas extras, horas negativas" [cite: 42]
    private String justificativa; // [cite: 39]
    private boolean justificado; // [cite: 41]
}