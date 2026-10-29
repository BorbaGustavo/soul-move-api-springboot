package br.com.soulMove.soul_move_api.dto;

import br.com.soulMove.soul_move_api.model.Ranking;

public record RankingResponseDTO(
        Long id,
        Integer posicao,
        Integer pontuacaoTotal,
        Long usuarioId,
        String nomeUsuario,
        String fotoUsuario
) {
    public RankingResponseDTO(Ranking ranking) {
        this(
                ranking.getId(),
                ranking.getPosicao(),
                ranking.getPontuacaoTotal(),
                ranking.getUsuario() != null ? ranking.getUsuario().getId() : null,
                ranking.getUsuario() != null ? ranking.getUsuario().getNome() : null,
                ranking.getUsuario() != null ? ranking.getUsuario().getFotoUsuario():null
        );
    }
}