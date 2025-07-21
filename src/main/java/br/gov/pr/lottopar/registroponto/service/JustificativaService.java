package br.gov.pr.lottopar.registroponto.service;

import br.gov.pr.lottopar.registroponto.model.Justificativa;
import br.gov.pr.lottopar.registroponto.model.Usuario;
import br.gov.pr.lottopar.registroponto.repository.JustificativaRepository;
import br.gov.pr.lottopar.registroponto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JustificativaService {

    @Autowired
    private JustificativaRepository justificativaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Justificativa criarJustificativa(Justificativa justificativa, String cpf) {
        Usuario usuario = usuarioRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        justificativa.setUser(usuario);
        justificativa.setStatus("pending");
        
        return justificativaRepository.save(justificativa);
    }

    public List<Justificativa> listarMinhasJustificativas(String cpf) {
        Usuario usuario = usuarioRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        return justificativaRepository.findByStatus(usuario.getCpf());
    }
}