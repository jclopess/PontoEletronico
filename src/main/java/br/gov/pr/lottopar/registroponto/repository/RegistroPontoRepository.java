package br.gov.pr.lottopar.registroponto.repository;

import br.gov.pr.lottopar.registroponto.model.RegistroPonto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, Long> {
    // Busca registros de um usuário específico entre uma data de início e fim
    List<RegistroPonto> findByUsuarioIdAndDataHoraBetweenOrderByDataHoraAsc(Long usuarioId, LocalDateTime inicio, LocalDateTime fim);
}