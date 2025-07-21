package br.gov.pr.lottopar.registroponto.service;

import br.gov.pr.lottopar.registroponto.model.Justificativa;
import br.gov.pr.lottopar.registroponto.model.RegistroPonto;
import br.gov.pr.lottopar.registroponto.model.Usuario;
import br.gov.pr.lottopar.registroponto.repository.JustificativaRepository;
import br.gov.pr.lottopar.registroponto.repository.RegistroPontoRepository;
import br.gov.pr.lottopar.registroponto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GestaoPontoService {

    @Autowired
    private RegistroPontoRepository registroPontoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JustificativaRepository justificativaRepository;

    public List<Usuario> listarSubordinados(String cpfGestor) {
        Usuario gestor = usuarioRepository.findByCpf(cpfGestor)
                .orElseThrow(() -> new SecurityException("Gestor não encontrado."));
        return usuarioRepository.findByGestorId(gestor.getId());
    }

    public List<RegistroPonto> listarRegistrosDoDia(String cpfGestor, LocalDate data) {
        List<Usuario> subordinados = listarSubordinados(cpfGestor);
        List<Long> idsSubordinados = subordinados.stream().map(Usuario::getId).collect(Collectors.toList());
        
        return registroPontoRepository.findByDate(data).stream()
                .filter(registro -> idsSubordinados.contains(registro.getUsuario().getId()))
                .collect(Collectors.toList());
    }

    public List<Justificativa> listarJustificativasPendentes(String cpfGestor) {
        List<Usuario> subordinados = listarSubordinados(cpfGestor);
        List<Long> idsSubordinados = subordinados.stream().map(Usuario::getId).collect(Collectors.toList());

        return justificativaRepository.findByStatus("pending").stream()
                .filter(justificativa -> idsSubordinados.contains(justificativa.getUser().getId()))
                .collect(Collectors.toList());
    }

    public Justificativa aprovarJustificativa(Long justificativaId, boolean aprovar, String cpfGestor) {
        Usuario gestor = usuarioRepository.findByCpf(cpfGestor)
                .orElseThrow(() -> new SecurityException("Gestor não encontrado."));

        Justificativa justificativa = justificativaRepository.findById(justificativaId)
                .orElseThrow(() -> new RuntimeException("Justificativa não encontrada."));

        // Validação de segurança: gestor só pode aprovar justificativas de seus subordinados
        if (justificativa.getUser().getGestor() == null || !justificativa.getUser().getGestor().getId().equals(gestor.getId())) {
            throw new SecurityException("Acesso negado. Você não é o gestor deste funcionário.");
        }

        justificativa.setStatus(aprovar ? "approved" : "rejected");
        justificativa.setApprovedBy(gestor);
        justificativa.setApprovedAt(LocalDateTime.now());
        
        return justificativaRepository.save(justificativa);
    }
}