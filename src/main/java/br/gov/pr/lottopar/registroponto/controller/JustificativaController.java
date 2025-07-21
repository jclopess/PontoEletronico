package br.gov.pr.lottopar.registroponto.controller;

import br.gov.pr.lottopar.registroponto.model.Justificativa;
import br.gov.pr.lottopar.registroponto.service.JustificativaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/justificativas")
@PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
public class JustificativaController {

    @Autowired
    private JustificativaService justificativaService;

    @PostMapping
    public ResponseEntity<Justificativa> criar(@RequestBody Justificativa justificativa) {
        String cpf = SecurityContextHolder.getContext().getAuthentication().getName();
        Justificativa novaJustificativa = justificativaService.criarJustificativa(justificativa, cpf);
        return ResponseEntity.status(201).body(novaJustificativa);
    }

    @GetMapping("/minhas")
    public ResponseEntity<List<Justificativa>> listarMinhas() {
        String cpf = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Justificativa> justificativas = justificativaService.listarMinhasJustificativas(cpf);
        return ResponseEntity.ok(justificativas);
    }
}