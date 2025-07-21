package br.gov.pr.lottopar.registroponto.controller;

import br.gov.pr.lottopar.registroponto.model.RegistroPonto;
import br.gov.pr.lottopar.registroponto.service.GestaoPontoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/gestao/ponto")
@PreAuthorize("hasRole('GESTOR')")
public class GestaoPontoController {

    @Autowired
    private GestaoPontoService gestaoPontoService;

    @PutMapping("/{id}/corrigir")
    public RegistroPonto corrigir(@PathVariable Long id, @RequestBody LocalDateTime novaDataHora) {
        String loginGestor = SecurityContextHolder.getContext().getAuthentication().getName();
        return gestaoPontoService.corrigirMarcacao(id, novaDataHora, loginGestor);
    }

    @PostMapping("/{id}/aprovar")
    public RegistroPonto aprovar(@PathVariable Long id) {
        String loginGestor = SecurityContextHolder.getContext().getAuthentication().getName();
        return gestaoPontoService.aprovarJustificativa(id, loginGestor);
    }
}
