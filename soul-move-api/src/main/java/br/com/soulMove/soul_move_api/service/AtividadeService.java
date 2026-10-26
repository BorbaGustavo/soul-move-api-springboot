package br.com.soulMove.soul_move_api.service;

import br.com.soulMove.soul_move_api.exception.RecursoNaoEncontradoException;
import br.com.soulMove.soul_move_api.exception.RegraNegocioException;
import br.com.soulMove.soul_move_api.model.Atividade;
import br.com.soulMove.soul_move_api.model.TiposAtividade;
import br.com.soulMove.soul_move_api.model.Usuario;
import br.com.soulMove.soul_move_api.model.UsuarioAtividade;
import br.com.soulMove.soul_move_api.repository.AtividadeRepository;
import br.com.soulMove.soul_move_api.repository.UsuarioAtividadeRepository;
import br.com.soulMove.soul_move_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@Service
public class AtividadeService {

    /** Dia contado no horário de Brasília (servidor em UTC viraria o dia às 21h). */
    private static final ZoneId ZONA = ZoneId.of("America/Sao_Paulo");

    @Autowired
    private UsuarioAtividadeRepository usuarioAtividadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AtividadeRepository atividadeRepository;

    @Autowired
    private RankingService rankingService;

    /** Quantas vezes o mesmo usuário pode registrar a mesma atividade no mesmo dia. */
    @Value("${soulmove.regras.max-registros-por-atividade-dia:99}")
    private long maxRegistrosPorDia;

    @Transactional
    public UsuarioAtividade registrarAtividade(Long usuarioId, Long atividadeId) {
        // FOR UPDATE: dois cliques simultâneos não furam o limite diário
        Usuario usuario = usuarioRepository.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + usuarioId));

        Atividade atividade = atividadeRepository.findById(atividadeId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atividade não encontrada com o ID: " + atividadeId));

        if (!atividade.validarAtividade()) {
            throw new RegraNegocioException("A atividade não cumpre as regras de validação necessárias.");
        }

        LocalDate hoje = LocalDate.now(ZONA);
        if (usuarioAtividadeRepository.contarNoDia(usuarioId, atividadeId, hoje) >= maxRegistrosPorDia) {
            throw new RegraNegocioException(
                    "Você já registrou '" + atividade.getNome() + "' hoje. Tente novamente amanhã.");
        }

        UsuarioAtividade registro = usuarioAtividadeRepository.save(
                new UsuarioAtividade(hoje, atividade.getPontuacao(), usuario, atividade));

        usuario.adicionarPontos(atividade.getPontuacao());   // pt_usuario += x e pt_soul += x
        rankingService.atualizarRanking(usuario);             // ranking sempre em dia

        return registro;
    }

    @Transactional
    public Atividade salvar(Atividade atividade) {
        if (atividade.getPontuacao() == null || atividade.getPontuacao() <= 0) {
            throw new IllegalArgumentException("A pontuação da atividade deve ser maior que zero.");
        }
        return atividadeRepository.save(atividade);
    }

    public List<Atividade> listarAtividadesDisponiveis() {
        return atividadeRepository.findAll();
    }

    public List<Atividade> listarPorTipo(TiposAtividade tipoAtividade) {
        return atividadeRepository.findByTipoAtividade(tipoAtividade);
    }

    public Atividade buscarPorId(Long id) {
        return atividadeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atividade não encontrada com o ID: " + id));
    }

    public List<TiposAtividade> listarTipos() {
        return Arrays.asList(TiposAtividade.values());
    }

    /** Histórico de atividades do usuário, mais recentes primeiro. */
    public List<UsuarioAtividade> obterHistoricoPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + usuarioId);
        }
        return usuarioAtividadeRepository.findByUsuarioIdOrderByDataAtividadeDescIdDesc(usuarioId);
    }
}


