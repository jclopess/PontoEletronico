package br.gov.pr.lottopar.registroponto.controller;

import br.gov.pr.lottopar.registroponto.model.Usuario;
import br.gov.pr.lottopar.registroponto.service.UsuarioService;
import br.gov.pr.lottopar.registroponto.dto.UsuarioInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/meus-subordinados")
    @PreAuthorize("hasRole('GESTOR')")
    public List<Usuario> getMeusSubordinados() {
        String cpfGestor = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioService.listarSubordinados(cpfGestor);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioInfoDTO> getUsuarioLogado() {
        String cpf = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.buscarPorCpf(cpf); // Método ajustado para buscar por CPF

        // Mapeia para o DTO com os nomes de campos CORRETOS
        UsuarioInfoDTO dto = new UsuarioInfoDTO();
        dto.setId(usuario.getId());
        dto.setName(usuario.getName()); // <- Corresponde ao `user.name` no frontend
        dto.setUsername(usuario.getUsername()); // <- Corresponde ao `user.username`
        dto.setRole(usuario.getRole()); // <- Corresponde ao `user.role`

        return ResponseEntity.ok(dto);
    }
}