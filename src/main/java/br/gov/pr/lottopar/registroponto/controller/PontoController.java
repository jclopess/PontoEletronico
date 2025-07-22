package br.gov.pr.lottopar.registroponto.controller;

import br.gov.pr.lottopar.registroponto.model.RegistroPonto;
import br.gov.pr.lottopar.registroponto.service.PontoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api/ponto")
public class PontoController {

    @Autowired
    private PontoService pontoService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public ResponseEntity<?> registrarPonto() { // Alterado para ResponseEntity<?> para permitir diferentes tipos de corpo
        String cpf = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            RegistroPonto registro = pontoService.registrarPonto(cpf);
            return ResponseEntity.ok(registro);
        } catch (RuntimeException e) {
            // CORREÇÃO: Retorna a mensagem de erro específica do service no corpo da resposta
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    @GetMapping("/meus-registros")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public List<RegistroPonto> getMeusRegistros(@RequestParam int ano, @RequestParam int mes) {
        String cpf = SecurityContextHolder.getContext().getAuthentication().getName();
        return pontoService.listarMeusRegistros(cpf, ano, mes);
    }
    @GetMapping("/hoje")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public ResponseEntity<RegistroPonto> getRegistroDeHoje() {
        String cpf = SecurityContextHolder.getContext().getAuthentication().getName();
        return pontoService.getRegistroDeHoje(cpf)
                .map(ResponseEntity::ok) // Se encontrar, retorna 200 OK com o registro
                .orElse(ResponseEntity.ok(null)); // Se não encontrar, retorna 200 OK com corpo nulo
}
}