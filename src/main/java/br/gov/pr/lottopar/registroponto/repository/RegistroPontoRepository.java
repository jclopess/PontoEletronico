package br.gov.pr.lottopar.registroponto.repository;

import br.gov.pr.lottopar.registroponto.model.RegistroPonto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, Long> {

    // CORRIGIDO: O nome do método agora reflete a busca pelo campo 'date' do tipo LocalDate.
    List<RegistroPonto> findByUsuarioIdAndDateBetweenOrderByDateAsc(Long usuarioId, LocalDate inicio, LocalDate fim);

    // Adicionado: Método para buscar o registro de um único dia.
    Optional<RegistroPonto> findByUsuarioIdAndDate(Long usuarioId, LocalDate date);
    
    // Adicionado: Método para buscar todos os registros de um dia (para o gestor).
    List<RegistroPonto> findByDate(LocalDate date);
}