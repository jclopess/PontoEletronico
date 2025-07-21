package br.gov.pr.lottopar.registroponto.controller;

import br.gov.pr.lottopar.registroponto.model.Departamento;
import br.gov.pr.lottopar.registroponto.service.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
@PreAuthorize("hasRole('ADMIN')") // Apenas usuários com perfil ADMIN podem acessar
public class DepartamentoController {
    @Autowired
    private DepartamentoService departamentoService;

    @PostMapping
    public ResponseEntity<Departamento> salvar(@RequestBody Departamento departamento) {
        Departamento savedDepartamento = departamentoService.salvar(departamento);
        return ResponseEntity.ok(savedDepartamento);
    }

    @GetMapping
    public ResponseEntity<List<Departamento>> listarTodos() {
        List<Departamento> departamentos = departamentoService.listarTodos();
        return ResponseEntity.ok(departamentos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        departamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}