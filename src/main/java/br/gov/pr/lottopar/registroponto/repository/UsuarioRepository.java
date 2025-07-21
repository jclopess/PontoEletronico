package br.gov.pr.lottopar.registroponto.repository;

import br.gov.pr.lottopar.registroponto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // O Spring Security usará este método para buscar um usuário pelo CPF
    Optional<Usuario> findByCpf(String cpf);

    List<Usuario> findByGestorId(Long gestorId);
}