package br.gov.pr.lottopar.registroponto;

import br.gov.pr.lottopar.registroponto.model.Perfil;
import br.gov.pr.lottopar.registroponto.model.Usuario;
import br.gov.pr.lottopar.registroponto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existe algum usuário para não duplicar os dados a cada reinicialização
        if (usuarioRepository.count() == 0) {
            System.out.println("Nenhum usuário encontrado, criando usuário ADMIN padrão...");

            Usuario admin = new Usuario();
            admin.setNomeCompleto("Administrador do Sistema");
            admin.setLogin("admin");
            // A senha deve ser sempre codificada antes de salvar
            admin.setSenha(passwordEncoder.encode("admin"));
            admin.setPerfil(Perfil.ADMIN);
            admin.setDataCadastro(LocalDate.now());

            usuarioRepository.save(admin);

            System.out.println("Usuário ADMIN criado com sucesso. Login: admin / Senha: admin");
        } else {
            System.out.println("Usuário administrador já existe, não foi criado um novo usuário.");
        }
    }
}