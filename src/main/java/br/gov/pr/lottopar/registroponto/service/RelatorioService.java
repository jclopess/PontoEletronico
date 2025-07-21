package br.gov.pr.lottopar.registroponto.service;

import br.gov.pr.lottopar.registroponto.dto.*;
import br.gov.pr.lottopar.registroponto.model.*;
import br.gov.pr.lottopar.registroponto.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;


@Service
public class RelatorioService {

   @Autowired private UsuarioRepository usuarioRepository;
   @Autowired private RegistroPontoRepository registroPontoRepository;


    public String gerarRelatorioMensal(Long funcionarioId, int ano, int mes, String loginGestor) {
        // Passo 1: Validar se o 'loginGestor' é o gestor do 'funcionarioId'.
        // (Lógica similar à do GestaoPontoService)

        // Passo 2: Buscar todos os registros de ponto para o funcionário no mês/ano.

        // Passo 3: Calcular horas trabalhadas, saldo de banco de horas, etc. [cite: 16, 17]

        // Passo 4: Montar e retornar o relatório (pode ser um DTO complexo ou uma String/PDF).
        return "Relatório para o funcionário " + funcionarioId + " do mês " + mes + "/" + ano;
    }
    
    public EspelhoPontoDTO gerarEspelhoPonto(Long funcionarioId, LocalDate dataInicio, LocalDate dataFim, String loginSolicitante) {
        // --- 1. LÓGICA DE AUTORIZAÇÃO ---
        // Garante que o solicitante tem permissão para ver este relatório [cite: 53]
        Usuario solicitante = usuarioRepository.findByLogin(loginSolicitante).orElseThrow();
        Usuario funcionario = usuarioRepository.findById(funcionarioId).orElseThrow();

        switch (solicitante.getPerfil()) {
            case FUNCIONARIO:
                if (!solicitante.getId().equals(funcionarioId)) throw new SecurityException("Acesso negado.");
                break;
            case GESTOR:
                if (funcionario.getGestor() == null || !funcionario.getGestor().getId().equals(solicitante.getId())) {
                    throw new SecurityException("Acesso negado. Você não é o gestor deste funcionário.");
                }
                break;
            case ADMIN:
                // Admin pode ver tudo
                break;
        }

        // --- 2. BUSCA DE DADOS ---
        List<RegistroPonto> registrosDoPeriodo = registroPontoRepository.findByUsuarioIdAndDataHoraBetweenOrderByDataHoraAsc(
            funcionario.getId(),
            dataInicio.atStartOfDay(),
            dataFim.atTime(23, 59, 59)
        );

        // --- 3. PROCESSAMENTO E AGREGAÇÃO ---
        EspelhoPontoDTO espelhoPonto = new EspelhoPontoDTO();

        // Preencher cabeçalho [cite: 44, 45, 46, 56]
        espelhoPonto.setNomeFuncionario(funcionario.getNomeCompleto());
        espelhoPonto.setDepartamento(funcionario.getDepartamento().getNome());
        espelhoPonto.setMesReferencia(dataInicio.getMonth().toString() + "/" + dataInicio.getYear());
        // ... Lógica para formatar outras informações do cabeçalho ...

        // Agrupar registros por dia e calcular totais diários [cite: 37, 38, 40]
        // Esta é a lógica principal: iterar sobre os 'registrosDoPeriodo',
        // agrupar por dia, calcular a diferença entre as marcações de entrada/saída.
        // O resultado será uma List<RegistroDiarioDTO> a ser inserida em espelhoPonto.setRegistrosDiarios(...);

        // Preencher totais e pendências [cite: 47, 48, 49]
        TotalizacaoDTO totalizacao = new TotalizacaoDTO();
        // Lógica para calcular totais semanais e o total geral
        // Lógica para contar registros com 'aguardandoAnalise' == true
        long pendencias = registrosDoPeriodo.stream().filter(RegistroPonto::isAguardandoAnalise).count();
        if (pendencias > 0) {
            totalizacao.setPendenciaJustificativa("Existe " + pendencias + " Pendência(s) de Análise de Justificativas Junto a Chefia Imediata");
        }
        espelhoPonto.setTotalizacao(totalizacao);

        // Preencher metadados da geração [cite: 54]
        espelhoPonto.setRelatorioGeradoPor("Gerado por: " + solicitante.getLogin() + " em " + java.time.LocalDateTime.now());

        return espelhoPonto;
    }
}
