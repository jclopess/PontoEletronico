package br.gov.pr.lottopar.registroponto.service;

import br.gov.pr.lottopar.registroponto.model.TipoVinculo;
import br.gov.pr.lottopar.registroponto.repository.TipoVinculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TipoVinculoService {

    @Autowired
    private TipoVinculoRepository tipoVinculoRepository;

    public TipoVinculo salvar(TipoVinculo tipoVinculo) {
        return tipoVinculoRepository.save(tipoVinculo);
    }

    public List<TipoVinculo> listarTodos() {
        return tipoVinculoRepository.findAll();
    }

    public void deletar(Long id) {
        tipoVinculoRepository.deleteById(id);
    }
}