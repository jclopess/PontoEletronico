package br.gov.pr.lottopar.registroponto.dto;

import br.gov.pr.lottopar.registroponto.model.Perfil;
import lombok.Data;

@Data
public class UsuarioInfoDTO {
    private Long id;
    private String nomeCompleto;
    private String login;
    private Perfil perfil;
}