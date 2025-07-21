package br.gov.pr.lottopar.registroponto.service;


import br.gov.pr.lottopar.registroponto.model.RegistroPonto;
import br.gov.pr.lottopar.registroponto.model.Usuario;
import br.gov.pr.lottopar.registroponto.repository.RegistroPontoRepository;
import br.gov.pr.lottopar.registroponto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class PontoService {

    @Autowired
    private RegistroPontoRepository registroPontoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public RegistroPonto registrarPonto(String login, String localizacao) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        RegistroPonto novoRegistro = new RegistroPonto();
        novoRegistro.setUsuario(usuario);
        novoRegistro.setDataHora(LocalDateTime.now()); // Horário exato da marcação [cite: 4]
        novoRegistro.setLocalizacao(localizacao); // Localização da marcação [cite: 5]
        novoRegistro.setJustificado(false);
        novoRegistro.setAguardandoAnalise(false);

        return registroPontoRepository.save(novoRegistro);
    }
    public List<RegistroPonto> listarMeusRegistros(String login, int ano, int mes) {
    Usuario usuario = usuarioRepository.findByLogin(login)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    YearMonth yearMonth = YearMonth.of(ano, mes);
    LocalDateTime inicio = yearMonth.atDay(1).atStartOfDay();
    LocalDateTime fim = yearMonth.atEndOfMonth().atTime(23, 59, 59);

    return registroPontoRepository.findByUsuarioIdAndDataHoraBetweenOrderByDataHoraAsc(usuario.getId(), inicio, fim);
    }
    // Em PontoService.java
    public RegistroPonto adicionarJustificativa(Long registroId, String justificativa, String login) {
    Usuario usuario = usuarioRepository.findByLogin(login)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    RegistroPonto registro = registroPontoRepository.findById(registroId)
            .orElseThrow(() -> new RuntimeException("Registro de ponto não encontrado"));

    // Validação de segurança: garante que o usuário só pode justificar seus próprios registros
    if (!registro.getUsuario().getId().equals(usuario.getId())) {
        throw new SecurityException("Acesso negado. Você não pode justificar um registro que não é seu.");
    }

    registro.setJustificativa(justificativa); // [cite: 39]
    registro.setAguardandoAnalise(true); // Indica que a justificativa aguarda análise da chefia [cite: 49, 52]

    return registroPontoRepository.save(registro);
    }
}