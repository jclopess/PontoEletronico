package br.gov.pr.lottopar.registroponto.controller;

import br.gov.pr.lottopar.registroponto.dto.EspelhoPontoDTO;
import br.gov.pr.lottopar.registroponto.service.RelatorioService;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/relatorios")
@PreAuthorize("hasRole('GESTOR')")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/mensal/{funcionarioId}")
    public String getRelatorioMensal(
            @PathVariable Long funcionarioId,
            @RequestParam int ano,
            @RequestParam int mes) {
        String loginGestor = SecurityContextHolder.getContext().getAuthentication().getName();
        return relatorioService.gerarRelatorioMensal(funcionarioId, ano, mes, loginGestor);
    }
        @GetMapping("/espelho-ponto/{funcionarioId}")
    // A autorização é feita dentro do service, pois a regra é complexa demais para uma anotação simples.
    public EspelhoPontoDTO getEspelhoPonto(
            @PathVariable Long funcionarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        String loginSolicitante = SecurityContextHolder.getContext().getAuthentication().getName();
        return relatorioService.gerarEspelhoPonto(funcionarioId, dataInicio, dataFim, loginSolicitante);
    }
}