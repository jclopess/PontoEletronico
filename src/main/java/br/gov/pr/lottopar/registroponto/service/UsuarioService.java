package br.gov.pr.lottopar.registroponto.service;

import br.gov.pr.lottopar.registroponto.dto.UsuarioDTO;
import br.gov.pr.lottopar.registroponto.model.Departamento;
import br.gov.pr.lottopar.registroponto.model.Funcao;
import br.gov.pr.lottopar.registroponto.model.TipoVinculo;
import br.gov.pr.lottopar.registroponto.model.Usuario;
import br.gov.pr.lottopar.registroponto.repository.DepartamentoRepository;
import br.gov.pr.lottopar.registroponto.repository.FuncaoRepository;
import br.gov.pr.lottopar.registroponto.repository.TipoVinculoRepository;
import br.gov.pr.lottopar.registroponto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private DepartamentoRepository departamentoRepository;
    @Autowired private FuncaoRepository funcaoRepository;
    @Autowired private TipoVinculoRepository tipoVinculoRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public Usuario buscarPorCpf(String cpf) {
        return usuarioRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o CPF: " + cpf));
    }

    public List<Usuario> listarSubordinados(String cpfGestor) {
        Usuario gestor = buscarPorCpf(cpfGestor);
        return usuarioRepository.findByGestorId(gestor.getId());
    }

    public void iniciarRedefinicaoSenha(String cpf) {
        Usuario usuario = buscarPorCpf(cpf);
        Usuario gestor = usuario.getGestor();
        if (gestor == null) {
            throw new RuntimeException("Usuário não possui um gestor imediato configurado.");
        }
        // Lógica de notificação para o gestor
        System.out.println("NOTIFICAÇÃO: Gestor '" + gestor.getUsername() + "' notificado para redefinir a senha do usuário '" + usuario.getName() + "'.");
    }

    public String gerarSenhaTemporariaParaUsuario(String cpfUsuario) {
        Usuario usuario = buscarPorCpf(cpfUsuario);
        String senhaTemporaria = UUID.randomUUID().toString().substring(0, 8);
        usuario.setPassword(passwordEncoder.encode(senhaTemporaria));
        // O campo senhaTemporariaAtiva foi removido do modelo, se precisar, adicione-o de volta.
        // usuario.setSenhaTemporariaAtiva(true);
        usuarioRepository.save(usuario);
        return senhaTemporaria;
    }

    public void trocarSenha(String cpf, String novaSenha) {
        Usuario usuario = buscarPorCpf(cpf);
        usuario.setPassword(passwordEncoder.encode(novaSenha));
        // usuario.setSenhaTemporariaAtiva(false);
        usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario criarUsuarioAdmin(UsuarioDTO usuarioDTO) {
        Usuario novoUsuario = new Usuario();
        mapearDtoParaEntidade(usuarioDTO, novoUsuario);
        novoUsuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        return usuarioRepository.save(novoUsuario);
    }

    public Usuario atualizarUsuarioAdmin(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
        mapearDtoParaEntidade(usuarioDTO, usuarioExistente);

        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        }
        return usuarioRepository.save(usuarioExistente);
    }

    // Método auxiliar centralizado para mapear DTO -> Entidade

    private void mapearDtoParaEntidade(UsuarioDTO dto, Usuario entidade) {
        entidade.setName(dto.getName());
        entidade.setCpf(dto.getCpf());
        entidade.setUsername(dto.getUsername());
        entidade.setPhone(dto.getPhone());
        entidade.setRole(dto.getRole());
        entidade.setStatus(dto.getStatus());
        entidade.setAdmissionDate(dto.getAdmissionDate());

        if (dto.getDepartmentId() != null) {
            Departamento depto = departamentoRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Departamento não encontrado com ID: " + dto.getDepartmentId()));
            entidade.setDepartment(depto);
        }

        if (dto.getFunctionId() != null) {
            Funcao funcao = funcaoRepository.findById(dto.getFunctionId())
                    .orElseThrow(() -> new RuntimeException("Função não encontrada com ID: " + dto.getFunctionId()));
            entidade.setFunction(funcao);
        }

        if (dto.getEmploymentTypeId() != null) {
            TipoVinculo tipoVinculo = tipoVinculoRepository.findById(dto.getEmploymentTypeId())
                    .orElseThrow(() -> new RuntimeException("Tipo de Vínculo não encontrado com ID: " + dto.getEmploymentTypeId()));
            entidade.setEmploymentType(tipoVinculo);
            entidade.setDailyWorkHours(tipoVinculo.getDailyWorkHours());
        }

        // --- CORREÇÃO APLICADA AQUI ---
        if (dto.getGestorId() != null) {
            Usuario gestor = usuarioRepository.findById(dto.getGestorId()) // Corrigido de getGesstorId para getGestorId
                    .orElseThrow(() -> new RuntimeException("Gestor não encontrado com ID: " + dto.getGestorId()));
            entidade.setGestor(gestor); // Vincula a entidade do gestor encontrada
        } else {
            entidade.setGestor(null); // Garante que o gestor seja removido se o ID for nulo
        }
    }
}