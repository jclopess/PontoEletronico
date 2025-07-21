package br.gov.pr.lottopar.registroponto.service;

import br.gov.pr.lottopar.registroponto.model.RegistroPonto;
import br.gov.pr.lottopar.registroponto.repository.RegistroPontoRepository;
import br.gov.pr.lottopar.registroponto.repository.UsuarioRepository;
import br.gov.pr.lottopar.registroponto.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class GestaoPontoService {

    @Autowired
    private RegistroPontoRepository registroPontoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public RegistroPonto corrigirMarcacao(Long registroId, LocalDateTime novaDataHora, String loginGestor) {
        RegistroPonto registro = validarAcessoGestor(registroId, loginGestor);
        registro.setDataHora(novaDataHora);
        // Poderíamos adicionar um campo para registrar que foi uma correção
        return registroPontoRepository.save(registro);
    }

    public RegistroPonto aprovarJustificativa(Long registroId, String loginGestor) {
        RegistroPonto registro = validarAcessoGestor(registroId, loginGestor);
        registro.setJustificado(true);
        registro.setAguardandoAnalise(false);
        return registroPontoRepository.save(registro);
    }

    // Método privado de validação para evitar duplicação de código
    private RegistroPonto validarAcessoGestor(Long registroId, String loginGestor) {
        Usuario gestor = usuarioRepository.findByLogin(loginGestor)
                .orElseThrow(() -> new SecurityException("Gestor não encontrado."));

        RegistroPonto registro = registroPontoRepository.findById(registroId)
                .orElseThrow(() -> new RuntimeException("Registro de ponto não encontrado."));

        // Validação de segurança CRÍTICA
        if (!registro.getUsuario().getGestor().getId().equals(gestor.getId())) {
            throw new SecurityException("Acesso negado. Você não é o gestor deste funcionário.");
        }
        return registro;
    }
}
