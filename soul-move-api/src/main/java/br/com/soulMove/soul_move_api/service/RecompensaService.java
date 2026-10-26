package br.com.soulMove.soul_move_api.service;

import br.com.soulMove.soul_move_api.exception.RecursoNaoEncontradoException;
import br.com.soulMove.soul_move_api.exception.RegraNegocioException;
import br.com.soulMove.soul_move_api.model.Recompensa;
import br.com.soulMove.soul_move_api.model.Usuario;
import br.com.soulMove.soul_move_api.model.UsuarioRecompensa;
import br.com.soulMove.soul_move_api.repository.RecompensaRepository;
import br.com.soulMove.soul_move_api.repository.UsuarioRepository;
import br.com.soulMove.soul_move_api.repository.UsuarioRecompensaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class RecompensaService {


    private static final ZoneId ZONA = ZoneId.of("America/Sao_Paulo");

    @Autowired
    private RecompensaRepository recompensaRepository;

    @Autowired
    private UsuarioRecompensaRepository usuarioRecompensaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioRecompensa resgatarRecompensa(Long usuarioId, Long recompensaId) {
        // FOR UPDATE: dois resgates simultâneos não gastam o mesmo saldo duas vezes
        Usuario usuario = usuarioRepository.findByIdForUpdate(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + usuarioId));

        Recompensa recompensa = recompensaRepository.findById(recompensaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Recompensa não encontrada com o ID: " + recompensaId));

        if (!recompensa.podeReceberRecompensa(usuario.getPontuacaoSoul())) {
            throw new RegraNegocioException(
                    String.format("Pontos Soul insuficientes para resgatar '%s'. Necessário: %d pts | Disponível: %d pts",
                            recompensa.getDescricao(), recompensa.getValor(), usuario.getPontuacaoSoul()));
        }

        usuario.debitarPontosSoul(recompensa.getValor());   // só pt_soul; pt_usuario e ranking não mudam
        return usuarioRecompensaRepository.save(
                new UsuarioRecompensa(LocalDate.now(ZONA), usuario, recompensa));
    }

    public List<Recompensa> getRecompensasDisponiveis() {
        return recompensaRepository.findAllByOrderByValorAsc();
    }

    public Recompensa buscarPorId(Long id) {
        return recompensaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Recompensa não encontrada com o ID: " + id));
    }

    /** Histórico de resgates do usuário, mais recentes primeiro. */
    public List<UsuarioRecompensa> obterHistoricoPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + usuarioId);
        }
        return usuarioRecompensaRepository.findByUsuarioIdOrderByDataRecebimentoDescIdDesc(usuarioId);
    }
}