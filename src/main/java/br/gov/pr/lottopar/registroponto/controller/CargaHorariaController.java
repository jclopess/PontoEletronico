package br.gov.pr.lottopar.registroponto.controller;

import br.gov.pr.lottopar.registroponto.model.CargaHoraria;
import br.gov.pr.lottopar.registroponto.service.CargaHorariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/cargas-horarias")
@PreAuthorize("hasRole('ADMIN')") // Apenas usuários com perfil ADMIN podem acessar
public class CargaHorariaController {

    @Autowired
    private CargaHorariaService cargaHorariaService;

    @PostMapping
    public ResponseEntity<CargaHoraria> salvar(@RequestBody CargaHoraria cargaHoraria) {
        CargaHoraria savedCargaHoraria = cargaHorariaService.salvar(cargaHoraria);
        return ResponseEntity.ok(savedCargaHoraria);
    }

    @GetMapping
    public ResponseEntity<List<CargaHoraria>> listarTodos() {
        List<CargaHoraria> cargasHorarias = cargaHorariaService.listarTodos();
        return ResponseEntity.ok(cargasHorarias);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cargaHorariaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
