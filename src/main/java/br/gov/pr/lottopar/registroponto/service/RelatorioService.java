package br.gov.pr.lottopar.registroponto.service;

import br.gov.pr.lottopar.registroponto.dto.EspelhoPontoDTO;
import br.gov.pr.lottopar.registroponto.dto.RegistroDiarioDTO;
import br.gov.pr.lottopar.registroponto.dto.TotalizacaoDTO;
import br.gov.pr.lottopar.registroponto.model.Perfil;
import br.gov.pr.lottopar.registroponto.model.RegistroPonto;
import br.gov.pr.lottopar.registroponto.model.Usuario;
import br.gov.pr.lottopar.registroponto.repository.RegistroPontoRepository;
import br.gov.pr.lottopar.registroponto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RegistroPontoRepository registroPontoRepository;

    public EspelhoPontoDTO gerarEspelhoPonto(Long funcionarioId, LocalDate dataInicio, LocalDate dataFim, String cpfSolicitante) {
        // --- 1. LÓGICA DE AUTORIZAÇÃO ---
        Usuario solicitante = usuarioRepository.findByCpf(cpfSolicitante)
                .orElseThrow(() -> new SecurityException("Solicitante não encontrado."));
        Usuario funcionario = usuarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado."));

        // Valida se o solicitante tem permissão para ver o relatório
        validarPermissao(solicitante, funcionario);

        // --- 2. BUSCA DE DADOS ---
        List<RegistroPonto> registrosDoPeriodo = registroPontoRepository.findByUsuarioIdAndDateBetweenOrderByDateAsc(
                funcionario.getId(), dataInicio, dataFim);

        // --- 3. PROCESSAMENTO E MONTAGEM DO DTO ---
        EspelhoPontoDTO espelhoPonto = new EspelhoPontoDTO();
        preencherCabecalho(espelhoPonto, funcionario, dataInicio);

        // Agrupa os registros por dia e cria os DTOs diários
        Map<LocalDate, List<RegistroPonto>> registrosAgrupadosPorDia = registrosDoPeriodo.stream()
                .collect(Collectors.groupingBy(RegistroPonto::getDate));

        List<RegistroDiarioDTO> registrosDiarios = new ArrayList<>();
        registrosAgrupadosPorDia.forEach((data, registros) -> {
            registrosDiarios.add(criarRegistroDiarioDTO(data, registros.get(0)));
        });
        espelhoPonto.setRegistrosDiarios(registrosDiarios);

        // Calcula e preenche os totais
        preencherTotalizacao(espelhoPonto, registrosDiarios);

        // Preenche metadados da geração
        espelhoPonto.setRelatorioGeradoPor("Gerado por: " + solicitante.getUsername() + " em " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        return espelhoPonto;
    }

    private void validarPermissao(Usuario solicitante, Usuario funcionario) {
        if (solicitante.getRole() == Perfil.EMPLOYEE && !solicitante.getId().equals(funcionario.getId())) {
            throw new SecurityException("Acesso negado. Funcionários só podem ver o próprio espelho de ponto.");
        }
        if (solicitante.getRole() == Perfil.MANAGER) {
            if (funcionario.getGestor() == null || !funcionario.getGestor().getId().equals(solicitante.getId())) {
                throw new SecurityException("Acesso negado. Você não é o gestor deste funcionário.");
            }
        }
        // Se for ADMIN, o acesso é permitido implicitamente.
    }

    private void preencherCabecalho(EspelhoPontoDTO espelho, Usuario funcionario, LocalDate dataInicio) {
        espelho.setNomeFuncionario(funcionario.getName());
        espelho.setDepartamento(funcionario.getDepartment() != null ? funcionario.getDepartment().getNome() : "Não informado");
        espelho.setMesReferencia(dataInicio.getMonth().getDisplayName(TextStyle.FULL, Locale.of("pt", "BR")) + "/" + dataInicio.getYear());
        espelho.setNomeGestor(funcionario.getGestor() != null ? funcionario.getGestor().getName() : "Não informado");
        // Adicione aqui outros campos do cabeçalho se necessário
    }

    private RegistroDiarioDTO criarRegistroDiarioDTO(LocalDate data, RegistroPonto registro) {
        RegistroDiarioDTO diarioDTO = new RegistroDiarioDTO();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String diaDaSemana = data.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.of("pt", "BR"));
        diarioDTO.setDia(data.format(DateTimeFormatter.ofPattern("dd/MM")) + " - " + diaDaSemana);

        List<String> marcacoes = new ArrayList<>();
        if (registro.getEntry1() != null) marcacoes.add(registro.getEntry1().format(timeFormatter));
        if (registro.getExit1() != null) marcacoes.add(registro.getExit1().format(timeFormatter));
        if (registro.getEntry2() != null) marcacoes.add(registro.getEntry2().format(timeFormatter));
        if (registro.getExit2() != null) marcacoes.add(registro.getExit2().format(timeFormatter));
        diarioDTO.setMarcacoes(marcacoes);

        if (registro.getTotalHours() != null) {
            long totalMinutes = registro.getTotalHours().multiply(new BigDecimal("60")).longValue();
            long hours = totalMinutes / 60;
            long minutes = totalMinutes % 60;
            diarioDTO.setTotalHorasTrabalhadasDia(String.format("%02d:%02d", hours, minutes));
        } else {
            diarioDTO.setTotalHorasTrabalhadasDia("Incompleto");
        }
        
        // Lógica de justificativa (simplificada, pode ser expandida)
        // diarioDTO.setJustificativa(...);
        // diarioDTO.setJustificado(...);

        return diarioDTO;
    }

    private void preencherTotalizacao(EspelhoPontoDTO espelho, List<RegistroDiarioDTO> registrosDiarios) {
        TotalizacaoDTO totalizacao = new TotalizacaoDTO();
        long totalMinutosGeral = 0;

        for (RegistroDiarioDTO dia : registrosDiarios) {
            if (!"Incompleto".equals(dia.getTotalHorasTrabalhadasDia()) && dia.getTotalHorasTrabalhadasDia() != null) {
                String[] partes = dia.getTotalHorasTrabalhadasDia().split(":");
                totalMinutosGeral += Long.parseLong(partes[0]) * 60 + Long.parseLong(partes[1]);
            }
        }

        long hours = totalMinutosGeral / 60;
        long minutes = totalMinutosGeral % 60;
        totalizacao.setTotalGeralHoras(String.format("%02d:%02d", hours, minutes));
        
        espelho.setTotalizacao(totalizacao);
    }
}