package br.gov.pr.lottopar.registroponto.controller;

import br.gov.pr.lottopar.registroponto.model.RegistroPonto;
import br.gov.pr.lottopar.registroponto.service.PontoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/ponto")
// Garante que apenas usuários com algum destes perfis possam acessar.
// Um gestor também é um funcionário e pode precisar bater seu próprio ponto.
@PreAuthorize("hasAnyRole('FUNCIONARIO', 'GESTOR', 'ADMIN')")
public class PontoController {

    @Autowired
    private PontoService pontoService;

    @PostMapping("/registrar")
    public RegistroPonto registrar(@RequestBody Map<String, String> payload) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        String localizacao = payload.get("localizacao");
        return pontoService.registrarPonto(login, localizacao);
    }
    @GetMapping("/meus-registros")
        public List<RegistroPonto> getMeusRegistros(@RequestParam int ano, @RequestParam int mes) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return pontoService.listarMeusRegistros(login, ano, mes);
    }
    // Em PontoController.java

    @PostMapping("/{id}/justificar")
    public RegistroPonto justificar(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        String justificativa = payload.get("justificativa");
        return pontoService.adicionarJustificativa(id, justificativa, login);
    }
}
