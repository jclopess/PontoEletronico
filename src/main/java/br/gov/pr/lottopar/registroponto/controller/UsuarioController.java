package br.gov.pr.lottopar.registroponto.controller;

import br.gov.pr.lottopar.registroponto.dto.UsuarioDTO;
import br.gov.pr.lottopar.registroponto.model.Usuario;
import br.gov.pr.lottopar.registroponto.service.UsuarioService;
import br.gov.pr.lottopar.registroponto.dto.UsuarioInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN', 'GESTOR')")
    public Usuario criarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.salvarUsuario(usuarioDTO);
    }

    @GetMapping("/meus-subordinados")
    @PreAuthorize("hasRole('GESTOR')")
    public List<Usuario> getMeusSubordinados() {
    // Pega o login do gestor autenticado no momento
        String loginGestor = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioService.listarSubordinados(loginGestor);
    }
    @GetMapping("/me")
    public ResponseEntity<UsuarioInfoDTO> getUsuarioLogado() {
    // Pega o login do usuário autenticado
    String login = SecurityContextHolder.getContext().getAuthentication().getName();
    
    // Busca o usuário completo no banco de dados
    Usuario usuario = usuarioService.buscarPorLogin(login); // Você precisará criar este método no service
    
    // Mapeia para o DTO
    UsuarioInfoDTO dto = new UsuarioInfoDTO();
    dto.setId(usuario.getId());
    dto.setNomeCompleto(usuario.getNomeCompleto());
    dto.setLogin(usuario.getLogin());
    dto.setPerfil(usuario.getPerfil());
    
    return ResponseEntity.ok(dto);
}
}