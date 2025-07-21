package br.gov.pr.lottopar.registroponto.service;

import br.gov.pr.lottopar.registroponto.model.Funcao;
import br.gov.pr.lottopar.registroponto.repository.FuncaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FuncaoService {

    @Autowired
    private FuncaoRepository funcaoRepository;

    public Funcao salvar(Funcao funcao) {
        return funcaoRepository.save(funcao);
    }

    public List<Funcao> listarTodos() {
        return funcaoRepository.findAll();
    }

    public void deletar(Long id) {
        funcaoRepository.deleteById(id);
    }

}
