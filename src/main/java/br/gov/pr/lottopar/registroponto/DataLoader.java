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
        // Verifica se o usuário admin já existe pelo CPF para não duplicar
        if (usuarioRepository.findByCpf("000.000.000-00").isEmpty()) {
            System.out.println("Nenhum usuário ADMIN padrão encontrado, criando...");

            Usuario admin = new Usuario();

            // Usando os setters corretos da entidade Usuario refatorada
            admin.setName("Administrador"); // Em vez de setNomeCompleto
            admin.setCpf("000.000.000-00");   // CPF preenchido com zeros
            admin.setUsername("admin");        // Em vez de setLogin
            admin.setPassword(passwordEncoder.encode("admin")); // Em vez de setSenha
            admin.setRole(Perfil.ADMIN);       // Em vez de setPerfil

            // Adicionando valores padrão para campos obrigatórios
            admin.setStatus("active");
            admin.setDailyWorkHours(new BigDecimal("8.00"));
            admin.setCreatedAt(LocalDate.now());

            usuarioRepository.save(admin);

            System.out.println("Usuário ADMIN criado com sucesso. CPF: 000.000.000-00 / Senha: admin");
        } else {
            System.out.println("Usuário administrador padrão já existe.");
        }
    }
}