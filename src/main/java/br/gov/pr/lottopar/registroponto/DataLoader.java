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
        if (usuarioRepository.findByCpf("0").isEmpty()) {
            System.out.println("Nenhum usuário ADMIN padrão encontrado, criando...");
            Usuario admin = new Usuario();
            admin.setName("Administrador");
            admin.setCpf("0");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Perfil.ADMIN);
            admin.setStatus("active");
            admin.setCreatedAt(LocalDate.now());
            admin.setDailyWorkHours(new BigDecimal("8.00"));
            usuarioRepository.save(admin);
            System.out.println("Usuário ADMIN criado com sucesso. CPF: 0 / Senha: 123456");
        }
        if (usuarioRepository.findByCpf("1").isEmpty()) {
            System.out.println("Nenhum usuário EMPLOYEE padrão encontrado, criando...");
            Usuario employee = new Usuario();
            employee.setName("Funcionário Padrão");
            employee.setCpf("1");
            employee.setUsername("employee");
            employee.setPassword(passwordEncoder.encode("employee"));
            employee.setRole(Perfil.EMPLOYEE);
            employee.setStatus("active");
            employee.setCreatedAt(LocalDate.now());
            employee.setDailyWorkHours(new BigDecimal("8.00"));
            usuarioRepository.save(employee);
            System.out.println("Usuário EMPLOYEE criado com sucesso. CPF: 1 / Senha: 123456");
        }
        if (usuarioRepository.findByCpf("2").isEmpty()) {
            System.out.println("Nenhum usuário MANAGER padrão encontrado, criando...");
            Usuario manager = new Usuario();
            manager.setName("Gerente Padrão");
            manager.setCpf("2");
            manager.setUsername("manager");
            manager.setPassword(passwordEncoder.encode("manager"));
            manager.setRole(Perfil.MANAGER);
            manager.setStatus("active");
            manager.setCreatedAt(LocalDate.now());
            manager.setDailyWorkHours(new BigDecimal("8.00"));
            usuarioRepository.save(manager);
            System.out.println("Usuário MANAGER criado com sucesso. CPF: 2 / Senha: 123456");
        } else {
            System.out.println("Já existem usuários padrão cadastrados.");
        }
    }
}