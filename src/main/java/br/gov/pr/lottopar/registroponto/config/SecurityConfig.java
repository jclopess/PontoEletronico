package br.gov.pr.lottopar.registroponto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilita CSRF, comum para APIs stateless
            .authorizeHttpRequests(authorize -> authorize
                // Permite acesso público ao endpoint de login
                .requestMatchers("/api/login").permitAll()
                // Todas as outras requisições precisam de autenticação
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                // Define o endpoint que processa a autenticação
                .loginProcessingUrl("/api/login")
                // Handler para SUCESSO na autenticação
                .successHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.OK.value()); // Retorna status 200 OK
                })
                // Handler para FALHA na autenticação
                .failureHandler((request, response, exception) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value()); // Retorna status 401 Unauthorized
                })
            )
            .logout(logout -> logout
                .logoutUrl("/api/logout")
                // No sucesso do logout, apenas retorna status 200 OK
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
            )
            // Configura como o sistema deve agir quando um acesso não autenticado é negado
            .exceptionHandling(e -> e
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value()); // Retorna 401 para qualquer tentativa de acesso não autenticado a uma rota protegida
                })
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}