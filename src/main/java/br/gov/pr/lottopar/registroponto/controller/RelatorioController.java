package br.gov.pr.lottopar.registroponto.controller;

import br.gov.pr.lottopar.registroponto.dto.EspelhoPontoDTO;
import br.gov.pr.lottopar.registroponto.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/relatorios")
// A autorização foi movida para o método para permitir que funcionários acessem seus próprios relatórios
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    // Este é o único endpoint necessário para relatórios.
    // A autorização é feita dentro do service, pois a regra é complexa.
    @GetMapping("/espelho-ponto/{funcionarioId}")
    public EspelhoPontoDTO getEspelhoPonto(
            @PathVariable Long funcionarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        String cpfSolicitante = SecurityContextHolder.getContext().getAuthentication().getName();
        return relatorioService.gerarEspelhoPonto(funcionarioId, dataInicio, dataFim, cpfSolicitante);
    }
}