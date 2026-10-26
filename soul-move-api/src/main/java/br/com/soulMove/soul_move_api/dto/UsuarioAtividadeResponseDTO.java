package br.com.soulMove.soul_move_api.dto;

import br.com.soulMove.soul_move_api.model.UsuarioAtividade;
import java.time.LocalDate;

public record UsuarioAtividadeResponseDTO(
        Long id,
        LocalDate dataAtividade,
        Integer pontosRecebida,
        String nomeUsuario,
        String nomeAtividade
) {
    public UsuarioAtividadeResponseDTO(UsuarioAtividade ua) {
        this(
                ua.getId(),
                ua.getDataAtividade(),
                ua.getPontosRecebida(),
                ua.getUsuario().getNome(),
                ua.getAtividade().getNome()
        );
    }
}