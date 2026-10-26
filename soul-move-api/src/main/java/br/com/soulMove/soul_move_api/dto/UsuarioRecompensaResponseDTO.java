package br.com.soulMove.soul_move_api.dto;

import br.com.soulMove.soul_move_api.model.UsuarioRecompensa;
import java.time.LocalDate;

public record UsuarioRecompensaResponseDTO(
        Long id,
        LocalDate dataRecebimento,
        String nomeUsuario,
        String descricaoRecompensa,
        Integer valor
) {
    public UsuarioRecompensaResponseDTO(UsuarioRecompensa ur) {
        this(
                ur.getId(),
                ur.getDataRecebimento(),
                ur.getUsuario().getNome(),
                ur.getRecompensa().getDescricao(),
                ur.getRecompensa().getValor()
        );
    }
}