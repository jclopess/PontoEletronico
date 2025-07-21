package br.gov.pr.lottopar.registroponto.controller;

import br.gov.pr.lottopar.registroponto.model.Justificativa;
import br.gov.pr.lottopar.registroponto.model.RegistroPonto;
import br.gov.pr.lottopar.registroponto.model.Usuario;
import br.gov.pr.lottopar.registroponto.service.GestaoPontoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gestao")
@PreAuthorize("hasAnyRole('GESTOR', 'ADMIN')") // Apenas Gestores e Admins podem acessar
public class GestaoPontoController {

    @Autowired
    private GestaoPontoService gestaoPontoService;

    // Endpoint para listar os funcionários da equipe do gestor
    @GetMapping("/equipe")
    public ResponseEntity<List<Usuario>> listarEquipe() {
        String cpfGestor = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Usuario> equipe = gestaoPontoService.listarSubordinados(cpfGestor);
        return ResponseEntity.ok(equipe);
    }

    // Endpoint para ver os registros de ponto da equipe em um dia específico
    @GetMapping("/registros-dia")
    public ResponseEntity<List<RegistroPonto>> getRegistrosDoDia(@RequestParam String data) {
        String cpfGestor = SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDate localDate = LocalDate.parse(data); // Espera data no formato "YYYY-MM-DD"
        List<RegistroPonto> registros = gestaoPontoService.listarRegistrosDoDia(cpfGestor, localDate);
        return ResponseEntity.ok(registros);
    }

    // Endpoint para listar as justificativas pendentes de aprovação
    @GetMapping("/justificativas-pendentes")
    public ResponseEntity<List<Justificativa>> getJustificativasPendentes() {
        String cpfGestor = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Justificativa> justificativas = gestaoPontoService.listarJustificativasPendentes(cpfGestor);
        return ResponseEntity.ok(justificativas);
    }

    // Endpoint para aprovar ou rejeitar uma justificativa
    @PostMapping("/justificativas/{id}/avaliar")
    public ResponseEntity<Justificativa> avaliarJustificativa(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {
        String cpfGestor = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean aprovar = payload.get("aprovar");
        try {
            Justificativa justificativa = gestaoPontoService.aprovarJustificativa(id, aprovar, cpfGestor);
            return ResponseEntity.ok(justificativa);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build(); // Forbidden
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build(); // Not Found
        }
    }
}