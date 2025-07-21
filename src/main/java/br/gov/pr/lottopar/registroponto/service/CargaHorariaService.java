package br.gov.pr.lottopar.registroponto.service;

import br.gov.pr.lottopar.registroponto.model.CargaHoraria;
import br.gov.pr.lottopar.registroponto.repository.CargaHorariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CargaHorariaService {

    @Autowired
    private CargaHorariaRepository cargaHorariaRepository;

    public CargaHoraria salvar(CargaHoraria cargaHoraria) {
        return cargaHorariaRepository.save(cargaHoraria);
    }

    public List<CargaHoraria> listarTodos() {
        return cargaHorariaRepository.findAll();
    }

    public void deletar(Long id) {
        cargaHorariaRepository.deleteById(id);
    }
}
