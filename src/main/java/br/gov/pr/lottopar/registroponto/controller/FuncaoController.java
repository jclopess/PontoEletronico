package br.gov.pr.lottopar.registroponto.controller;

import br.gov.pr.lottopar.registroponto.model.Funcao;
import br.gov.pr.lottopar.registroponto.service.FuncaoService; // Você precisa criar este service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/funcoes")
@PreAuthorize("hasRole('ADMIN')")
public class FuncaoController {

    @Autowired
    private FuncaoService funcaoService;

    @PostMapping
    public Funcao criar(@RequestBody Funcao funcao) {
        return funcaoService.salvar(funcao);
    }

    @GetMapping
    public List<Funcao> listar() {
        return funcaoService.listarTodos();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        funcaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}