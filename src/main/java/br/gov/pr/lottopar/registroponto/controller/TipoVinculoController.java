package br.gov.pr.lottopar.registroponto.controller;

import br.gov.pr.lottopar.registroponto.model.TipoVinculo;
import br.gov.pr.lottopar.registroponto.service.TipoVinculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-vinculo") // Endpoint atualizado
@PreAuthorize("hasRole('ADMIN')")
public class TipoVinculoController {

    @Autowired
    private TipoVinculoService tipoVinculoService; // Service atualizado

    @PostMapping
    public ResponseEntity<TipoVinculo> salvar(@RequestBody TipoVinculo tipoVinculo) {
        TipoVinculo savedTipoVinculo = tipoVinculoService.salvar(tipoVinculo);
        return ResponseEntity.ok(savedTipoVinculo);
    }

    @GetMapping
    public ResponseEntity<List<TipoVinculo>> listarTodos() {
        List<TipoVinculo> tiposVinculo = tipoVinculoService.listarTodos();
        return ResponseEntity.ok(tiposVinculo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tipoVinculoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}