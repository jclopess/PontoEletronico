package br.gov.pr.lottopar.registroponto.repository;

import br.gov.pr.lottopar.registroponto.model.Justificativa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JustificativaRepository extends JpaRepository <Justificativa, Long> {
    
}
