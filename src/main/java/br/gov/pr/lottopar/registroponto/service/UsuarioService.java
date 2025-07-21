package br.gov.pr.lottopar.registroponto.service;

import br.gov.pr.lottopar.registroponto.dto.UsuarioDTO;
import br.gov.pr.lottopar.registroponto.model.*; // Importe todos os modelos
import br.gov.pr.lottopar.registroponto.repository.DepartamentoRepository;
import br.gov.pr.lottopar.registroponto.repository.FuncaoRepository;
import br.gov.pr.lottopar.registroponto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {
    @Autowired
    private FuncaoRepository funcaoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listarSubordinados(String loginGestor) {
        Usuario gestor = usuarioRepository.findByLogin(loginGestor)
            .orElseThrow(() -> new RuntimeException("Gestor não encontrado"));
        return usuarioRepository.findByGestorId(gestor.getId());
    }

    // Método a ser chamado quando um usuário clica em "Esqueci minha senha" [cite: 29]
    public void iniciarRedefinicaoSenha(String login) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Usuario gestor = usuario.getGestor();
        if (gestor == null) {
            throw new RuntimeException("Usuário não possui um gestor imediato configurado.");
        }

        // Lógica para notificar o gestor [cite: 30]
        // Em um sistema real, aqui ocorreria um envio de e-mail ou notificação interna.
        System.out.println("NOTIFICAÇÃO: Gestor '" + gestor.getLogin() + "' notificado para redefinir a senha do usuário '" + login + "'.");
    }

    // Método a ser chamado pelo GESTOR para efetivamente gerar a nova senha [cite: 31]
    public String gerarSenhaTemporariaParaUsuario(String loginUsuario) {
        Usuario usuario = usuarioRepository.findByLogin(loginUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Gera uma senha temporária simples
        String senhaTemporaria = UUID.randomUUID().toString().substring(0, 8);

        usuario.setSenha(passwordEncoder.encode(senhaTemporaria));
        usuario.setSenhaTemporariaAtiva(true); // Marca que o usuário precisa trocar a senha no próximo login [cite: 32]

        usuarioRepository.save(usuario);

        // Retorna a senha para que o gestor possa informá-la ao funcionário
        return senhaTemporaria;
    }

    // Método para o próprio usuário trocar a senha (após logar com a temporária)
    public void trocarSenha(String login, String novaSenha) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setSenhaTemporariaAtiva(false); // Desativa o status de senha temporária
        usuarioRepository.save(usuario);
    }
    public Usuario salvarUsuario(UsuarioDTO usuarioDTO) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNomeCompleto(usuarioDTO.getNomeCompleto());
        novoUsuario.setLogin(usuarioDTO.getLogin());
        // Codifica a senha antes de salvar [cite: 62]
        novoUsuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        novoUsuario.setPerfil(usuarioDTO.getPerfil());
        novoUsuario.setDataCadastro(java.time.LocalDate.now());

    // Associa as entidades relacionadas pelos IDs
    Departamento depto = departamentoRepository.findById(usuarioDTO.getDepartamentoId()).orElse(null);
        novoUsuario.setDepartamento(depto);

    Funcao funcao = funcaoRepository.findById(usuarioDTO.getFuncaoId()).orElse(null);
        novoUsuario.setFuncao(funcao);
    
    // ATENÇÃO: Adicione os repositórios de Funcao, CargaHoraria e Departamento como @Autowired no service
    // ...
    // Faça o mesmo para CargaHoraria e Gestor (se aplicável)
    
    return usuarioRepository.save(novoUsuario);
    }
    public Usuario buscarPorLogin(String login) {
        return usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}