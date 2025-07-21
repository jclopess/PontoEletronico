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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // A variável 'username' é o que o usuário digita no campo de login.
        // Vamos tratá-la como o CPF.
        
        // 1. NORMALIZAÇÃO: Remove toda a pontuação do CPF digitado.
        String cpfNumerico = username.replaceAll("\\D", "");

        // 2. BUSCA: Usa o CPF numérico para buscar no banco.
        Usuario usuario = usuarioRepository.findByCpf(cpfNumerico)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o CPF: " + username));

        // 3. RETORNO: O Spring Security usará estas informações para comparar a senha.
        // O primeiro argumento (username) deve ser um identificador único, usamos o CPF.
        return new User(
                usuario.getCpf(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()))
        );
    }
}