package br.gov.pr.lottopar.registroponto;

import br.gov.pr.lottopar.registroponto.model.Perfil;
import br.gov.pr.lottopar.registroponto.model.Usuario;
import br.gov.pr.lottopar.registroponto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.findByCpf("00000000000").isEmpty()) {
            System.out.println("Nenhum usuário ADMIN padrão encontrado, criando...");

            Usuario admin = new Usuario();

            admin.setName("Administrador");
            admin.setCpf("00000000000");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Perfil.ADMIN);
            admin.setStatus("active");
            admin.setCreatedAt(LocalDate.now());
            admin.setDailyWorkHours(new BigDecimal("8.00"));

            usuarioRepository.save(admin);

            System.out.println("Usuário ADMIN criado com sucesso. CPF: 00000000000 / Senha: admin");
        } else {
            System.out.println("Usuário administrador padrão já existe.");
        }
    }
}