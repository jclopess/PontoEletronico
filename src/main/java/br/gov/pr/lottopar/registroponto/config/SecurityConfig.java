package br.gov.pr.lottopar.registroponto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Desabilita a proteção CSRF. É uma prática comum para APIs
            //    e simplifica nosso formulário de login que não tem o token CSRF.
            .csrf(csrf -> csrf.disable())

            // 2. Define as regras de autorização para os requests HTTP
            .authorizeHttpRequests(authorize -> authorize
                // Permite acesso a recursos estáticos e à página de esqueci-senha sem autenticação.
                // Note que REMOVEMOS o "/login" daqui.
                .requestMatchers("/css/**", "/js/**", "/images/**", "/esqueci-senha").permitAll()
                // Todas as outras requisições exigem autenticação
                .anyRequest().authenticated()
            )

            // 3. Configura o formulário de login
            .formLogin(form -> form
                // O endpoint que processa o POST com as credenciais. Deve ser o mesmo do 'action' do form.
                .loginProcessingUrl("/login")
                // A URL para onde o usuário é redirecionado após o sucesso
                .defaultSuccessUrl("/home.html", true)
                // A URL para onde o usuário é redirecionado em caso de falha
                .failureUrl("/index.html?error=true")
                // Permite que todos acessem os endpoints relacionados ao formulário de login.
                .permitAll()
            )

            // 4. Configura o logout
            .logout(logout -> logout
                .logoutUrl("/logout") // URL para acionar o logout
                .logoutSuccessUrl("/index.html?logout=true") // Para onde vai após o logout
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
