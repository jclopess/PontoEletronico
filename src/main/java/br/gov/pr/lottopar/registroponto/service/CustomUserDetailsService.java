package br.gov.pr.lottopar.registroponto.service;

import br.gov.pr.lottopar.registroponto.model.Usuario;
import br.gov.pr.lottopar.registroponto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        // Busca o usuário no banco de dados pelo CPF
        Usuario usuario = usuarioRepository.findByCpf(cpf)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o CPF: " + cpf));

        // Retorna um objeto UserDetails que o Spring Security usa para autenticação
        // O perfil (ex: GESTOR) é prefixado com "ROLE_" por convenção do Spring Security
        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()))
        );
    }
}