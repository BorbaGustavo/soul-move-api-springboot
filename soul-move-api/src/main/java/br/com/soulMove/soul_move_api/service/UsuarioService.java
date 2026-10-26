package br.com.soulMove.soul_move_api.service;

import br.com.soulMove.soul_move_api.dto.UsuarioUpdateRequestDTO;
import br.com.soulMove.soul_move_api.exception.CredenciaisInvalidasException;
import br.com.soulMove.soul_move_api.exception.RecursoNaoEncontradoException;
import br.com.soulMove.soul_move_api.exception.RegraNegocioException;
import br.com.soulMove.soul_move_api.model.Usuario;
import br.com.soulMove.soul_move_api.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class UsuarioService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RankingService rankingService;

    private static String normalizarEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }

    @Transactional
    public Usuario salvar(Usuario usuario) {
        usuario.setEmail(normalizarEmail(usuario.getEmail()));   // "A@x.com" e "a@x.com" são a mesma conta
        usuario.setNome(usuario.getNome().trim());

        if (usuario.getId() == null && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com o e-mail: " + usuario.getEmail());
        }
        if (usuario.getId() == null) {
            // pontos sempre nascem zerados: o cliente não escolhe a própria pontuação
            usuario.setPontuacaoUsuario(0);
            usuario.setPontuacaoSoul(0);
        }
        Usuario salvo = usuarioRepository.save(usuario);
        rankingService.atualizarRanking(salvo);   // todo usuário nasce com linha no ranking
        return salvo;
    }

    /** Login: confere e-mail e senha e devolve o usuário (o front guarda o id). */
    @Transactional(readOnly = true)
    public Usuario login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(normalizarEmail(email))
                .orElseThrow(CredenciaisInvalidasException::new);
        if (!usuario.getSenha().equals(senha)) {
            throw new CredenciaisInvalidasException();
        }
        return usuario;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + id));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(normalizarEmail(email))
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o e-mail: " + email));
    }

    public List<Usuario> pesquisarPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Usuario> listarRankingGeral() {
        return usuarioRepository.findAllByOrderByPontuacaoUsuarioDesc();
    }

    public List<Usuario> obterTop10Pontuadores() {
        return usuarioRepository.findTop10ByOrderByPontuacaoUsuarioDesc();
    }

    @Transactional
    public Usuario atualizarPerfil(Long id, UsuarioUpdateRequestDTO dto) {
        Usuario usuario = buscarPorId(id);

        // Normaliza o e-mail (minúsculo e sem espaços)
        String emailNormalizado = dto.email().trim().toLowerCase();

        // Se o e-mail foi alterado, verifica se já existe outro usuário com o mesmo e-mail
        if (!usuario.getEmail().equalsIgnoreCase(emailNormalizado)) {
            boolean emailExiste = usuarioRepository.findByEmail(emailNormalizado).isPresent();
            if (emailExiste) {
                throw new RegraNegocioException("O e-mail informado já está em uso por outra conta.");
            }
            usuario.setEmail(emailNormalizado);
        }

        // Atualiza nome
        usuario.setNome(dto.nome().trim());

        // Atualiza foto do perfil (se informada)
        usuario.setFotoUsuario(
                dto.fotoUsuario() == null || dto.fotoUsuario().isBlank() ? null : dto.fotoUsuario().trim()
        );

        // Atualiza senha apenas se foi preenchida
        if (dto.senha() != null && !dto.senha().isBlank()) {
            // Se usar PasswordEncoder: usuario.setSenha(passwordEncoder.encode(dto.senha().trim()));
            usuario.setSenha(dto.senha().trim());
        }

        return usuarioRepository.save(usuario);
    }

    /**
     * T_SM_USUARIO e T_SM_RANKING apontam um para o outro (FK circular). Por isso o vínculo é
     * desfeito e gravado ANTES de apagar as linhas — senão o banco recusa o DELETE.
     */
    @Transactional
    public void deletar(Long id) {
        Usuario usuario = usuarioRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + id));
        usuario.setRanking(null);
        usuarioRepository.flush();           // grava T_SM_RANKING_id_ranking = NULL
        rankingService.remover(id);          // apaga o ranking e reposiciona os demais
        usuarioRepository.delete(usuario);   // cascade apaga atividades e resgates do usuário
    }
}