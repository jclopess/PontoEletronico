package br.gov.pr.lottopar.registroponto.dto;

import br.gov.pr.lottopar.registroponto.model.Perfil;
import lombok.Data;

@Data
public class UsuarioDTO {
    private String nomeCompleto;
    private String login;
    private String senha;
    private Perfil perfil;
    private Long departamentoId;
    private Long funcaoId;
    private Long cargaHorariaId;
    private Long gestorId; // Opcional, para associar um funcionário a um gestor
}