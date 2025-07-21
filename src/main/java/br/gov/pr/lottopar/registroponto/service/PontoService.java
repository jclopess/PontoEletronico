package br.gov.pr.lottopar.registroponto.service;

import br.gov.pr.lottopar.registroponto.model.RegistroPonto;
import br.gov.pr.lottopar.registroponto.model.Usuario;
import br.gov.pr.lottopar.registroponto.repository.RegistroPontoRepository;
import br.gov.pr.lottopar.registroponto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import java.util.List;
import java.math.RoundingMode;

@Service
public class PontoService {

    @Autowired
    private RegistroPontoRepository registroPontoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public RegistroPonto registrarPonto(String cpf) {
        Usuario usuario = usuarioRepository.findByCpf(cpf) // Alterado para buscar por CPF
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        LocalDate hoje = LocalDate.now();
        LocalTime agora = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);

        // Busca o registro do dia ou cria um novo se não existir
        RegistroPonto registroDoDia = registroPontoRepository.findByUsuarioIdAndDate(usuario.getId(), hoje)
                .orElse(new RegistroPonto(usuario, hoje));

        // Lógica para preenchimento sequencial
        if (registroDoDia.getEntry1() == null) {
            registroDoDia.setEntry1(agora);
        } else if (registroDoDia.getExit1() == null) {
            validarIntervalo(registroDoDia.getEntry1(), agora);
            registroDoDia.setExit1(agora);
        } else if (registroDoDia.getEntry2() == null) {
            validarIntervalo(registroDoDia.getExit1(), agora);
            registroDoDia.setEntry2(agora);
        } else if (registroDoDia.getExit2() == null) {
            validarIntervalo(registroDoDia.getEntry2(), agora);
            registroDoDia.setExit2(agora);
            // Após a última marcação, calcula o total de horas
            calcularTotalHoras(registroDoDia);
        } else {
            throw new RuntimeException("Todos os 4 registros do dia já foram feitos.");
        }

        registroDoDia.setUpdatedAt(LocalDateTime.now());
        return registroPontoRepository.save(registroDoDia);
    }

    private void validarIntervalo(LocalTime anterior, LocalTime atual) {
        if (ChronoUnit.MINUTES.between(anterior, atual) < 60) {
            throw new RuntimeException("Intervalo mínimo de 1 hora entre as marcações não foi atendido.");
        }
    }

    private void calcularTotalHoras(RegistroPonto registro) {
        if (registro.getEntry1() != null && registro.getExit1() != null &&
            registro.getEntry2() != null && registro.getExit2() != null) {

            long minutosManha = ChronoUnit.MINUTES.between(registro.getEntry1(), registro.getExit1());
            long minutosTarde = ChronoUnit.MINUTES.between(registro.getEntry2(), registro.getExit2());

            double totalHoras = (minutosManha + minutosTarde) / 60.0;
            registro.setTotalHours(BigDecimal.valueOf(totalHoras).setScale(2, RoundingMode.HALF_UP));
        }
    }
    
    // Métodos para justificativas foram movidos para JustificativaService

    public List<RegistroPonto> listarMeusRegistros(String cpf, int ano, int mes) {
        Usuario usuario = usuarioRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // CORRIGIDO: Lógica agora usa LocalDate para construir o período
        YearMonth yearMonth = YearMonth.of(ano, mes);
        LocalDate inicio = yearMonth.atDay(1);
        LocalDate fim = yearMonth.atEndOfMonth();

        // CORRIGIDO: Chamando o novo método do repositório
        return registroPontoRepository.findByUsuarioIdAndDateBetweenOrderByDateAsc(usuario.getId(), inicio, fim);
    }
}