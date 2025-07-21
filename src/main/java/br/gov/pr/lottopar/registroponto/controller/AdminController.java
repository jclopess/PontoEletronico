package br.gov.pr.lottopar.registroponto.controller;

import br.gov.pr.lottopar.registroponto.dto.UsuarioDTO; // Crie este DTO
import br.gov.pr.lottopar.registroponto.model.*;
import br.gov.pr.lottopar.registroponto.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired private UsuarioService usuarioService;
    @Autowired private DepartamentoService departamentoService;
    @Autowired private FuncaoService funcaoService;
    @Autowired private TipoVinculoService tipoVinculoService; // Renomeado de CargaHorariaService

    // --- Gerenciamento de Usuários ---
    @GetMapping("/users")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarTodosUsuarios());
    }

    @PostMapping("/users")
    public ResponseEntity<Usuario> criarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario novoUsuario = usuarioService.criarUsuarioAdmin(usuarioDTO);
        return ResponseEntity.status(201).body(novoUsuario);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuarioAtualizado = usuarioService.atualizarUsuarioAdmin(id, usuarioDTO);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    // --- Gerenciamento de Departamentos ---
    @GetMapping("/departments")
    public ResponseEntity<List<Departamento>> listarDepartamentos() {
        return ResponseEntity.ok(departamentoService.listarTodos());
    }
    
    @PostMapping("/departments")
    public ResponseEntity<Departamento> criarDepartamento(@RequestBody Departamento departamento) {
        return ResponseEntity.status(201).body(departamentoService.salvar(departamento));
    }

    // --- Gerenciamento de Funções ---
    @GetMapping("/functions")
    public ResponseEntity<List<Funcao>> listarFuncoes() {
        return ResponseEntity.ok(funcaoService.listarTodos());
    }

    @PostMapping("/functions")
    public ResponseEntity<Funcao> criarFuncao(@RequestBody Funcao funcao) {
        return ResponseEntity.status(201).body(funcaoService.salvar(funcao));
    }
    
    // --- Gerenciamento de Vínculos (Employment Types) ---
    @GetMapping("/employment-types")
    public ResponseEntity<List<TipoVinculo>> listarTiposVinculo() {
        return ResponseEntity.ok(tipoVinculoService.listarTodos());
    }
    
    @PostMapping("/employment-types")
    public ResponseEntity<TipoVinculo> criarTipoVinculo(@RequestBody TipoVinculo tipoVinculo) {
        return ResponseEntity.status(201).body(tipoVinculoService.salvar(tipoVinculo));
    }

    // Adicionar aqui endpoints para PasswordResetRequests se necessário
}