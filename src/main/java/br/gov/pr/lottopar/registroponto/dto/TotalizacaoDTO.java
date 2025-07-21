package br.gov.pr.lottopar.registroponto.dto;

import lombok.Data;
import java.util.List;

@Data
public class TotalizacaoDTO {
    private List<String> totaisSemanais; // Ex: ["Semana 1: 27:32", "Semana 2: 39:33"] [cite: 47]
    private String totalGeralHoras; // Ex: "92:06" [cite: 48]
    private String pendenciaJustificativa; // Ex: "Existe 1 Pendência(s) de Análise..." [cite: 49]
}