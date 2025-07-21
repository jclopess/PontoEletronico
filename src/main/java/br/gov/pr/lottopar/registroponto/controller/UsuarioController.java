package br.gov.pr.lottopar.registroponto.controller;

// Removido o import do UsuarioDTO pois não é mais usado diretamente aqui
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

    // Este endpoint foi movido para o AdminController para centralizar as operações de admin.
    // @PostMapping
    // @PreAuthorize("hasRole('ADMIN', 'GESTOR')")
    // public Usuario criarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
    //     return usuarioService.salvarUsuario(usuarioDTO);
    // }

    @GetMapping("/meus-subordinados")
    @PreAuthorize("hasRole('GESTOR')")
    public List<Usuario> getMeusSubordinados() {
        // Pega o CPF do gestor autenticado no momento
        String cpfGestor = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioService.listarSubordinados(cpfGestor);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioInfoDTO> getUsuarioLogado() {
        // Pega o CPF do usuário autenticado
        String cpf = SecurityContextHolder.getContext().getAuthentication().getName();

        // Busca o usuário completo no banco de dados
        Usuario usuario = usuarioService.buscarPorCpf(cpf); // Método ajustado para buscar por CPF

        // Mapeia para o DTO com os nomes de campos CORRETOS
        UsuarioInfoDTO dto = new UsuarioInfoDTO();
        dto.setId(usuario.getId());
        dto.setName(usuario.getName()); // CORRIGIDO: de getNomeCompleto() para getName()
        dto.setLogin(usuario.getUsername());    // CORRIGIDO: de getLogin() para getUsername()
        dto.setPerfil(usuario.getRole());       // CORRIGIDO: de getPerfil() para getRole()

        return ResponseEntity.ok(dto);
    }
}