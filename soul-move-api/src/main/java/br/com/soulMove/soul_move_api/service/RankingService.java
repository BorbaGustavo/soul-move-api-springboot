package br.com.soulMove.soul_move_api.service;

import br.com.soulMove.soul_move_api.exception.RecursoNaoEncontradoException;
import br.com.soulMove.soul_move_api.model.Ranking;
import br.com.soulMove.soul_move_api.model.Usuario;
import br.com.soulMove.soul_move_api.repository.RankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RankingService {

    @Autowired
    private RankingRepository rankingRepository;

    /**
     * Garante a linha de ranking do usuário, copia a pontuação atual e recalcula TODAS as posições.
     * Chamar sempre que pt_usuario mudar (cadastro e registro de atividade).
     */
    @Transactional
    public Ranking atualizarRanking(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            throw new IllegalArgumentException("Usuário informado é inválido ou não possui cadastro.");
        }
        List<Ranking> todos = new ArrayList<>(rankingRepository.findTodosParaRecalculo());

        Ranking meu = rankingRepository.findByUsuarioId(usuario.getId()).orElse(null);
        if (meu == null) {
            meu = rankingRepository.save(new Ranking(1, usuario.getPontuacaoUsuario(), usuario));
            todos.add(meu);
        }
        if (usuario.getRanking() == null) {
            usuario.setRanking(meu);   // preenche T_SM_USUARIO.T_SM_RANKING_id_ranking (antes ficava NULL)
        }
        meu.setPontuacaoTotal(usuario.getPontuacaoUsuario());

        recalcularPosicoes(todos);
        return meu;
    }

    /** Apaga a linha do ranking do usuário e reposiciona os outros (usado ao excluir a conta). */
    @Transactional
    public void remover(Long usuarioId) {
        List<Ranking> todos = new ArrayList<>(rankingRepository.findTodosParaRecalculo());
        rankingRepository.findByUsuarioId(usuarioId).ifPresent(meu -> {
            todos.removeIf(r -> r.getId().equals(meu.getId()));
            rankingRepository.delete(meu);
        });
        recalcularPosicoes(todos);
    }

    private void recalcularPosicoes(List<Ranking> rankings) {
        List<Ranking> ordenados = new ArrayList<>(rankings);
        ordenados.sort(Comparator.comparing(Ranking::getPontuacaoTotal).reversed());
        int posicao = 0, indice = 0;
        Integer anterior = null;
        for (Ranking r : ordenados) {
            indice++;
            if (!r.getPontuacaoTotal().equals(anterior)) {
                posicao = indice;
                anterior = r.getPontuacaoTotal();
            }
            r.setPosicao(posicao);
        }
    }

    public Optional<Ranking> buscarPorUsuario(Long usuarioId) {
        if (usuarioId == null || usuarioId <= 0) {
            throw new IllegalArgumentException("ID do usuário informado é inválido.");
        }
        return rankingRepository.findByUsuarioId(usuarioId);
    }

    public Ranking buscarPorUsuarioOuFalhar(Long usuarioId) {
        return buscarPorUsuario(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ranking não encontrado para o usuário " + usuarioId));
    }

    public List<Ranking> obterRankingCompleto() {
        return rankingRepository.findAllByOrderByPontuacaoTotalDesc();
    }
}