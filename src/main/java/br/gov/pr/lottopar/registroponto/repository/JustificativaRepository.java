package br.gov.pr.lottopar.registroponto.repository;

import br.gov.pr.lottopar.registroponto.model.Justificativa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JustificativaRepository extends JpaRepository <Justificativa, Long> {
    List<Justificativa> findByStatus(String status);
}
