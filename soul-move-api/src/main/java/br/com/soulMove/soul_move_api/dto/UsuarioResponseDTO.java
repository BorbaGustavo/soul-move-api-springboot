package br.com.soulMove.soul_move_api.dto;

import br.com.soulMove.soul_move_api.model.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String fotoUsuario,
        Integer pontuacaoUsuario,
        Integer pontuacaoSoul
) {
    // Construtor utilitário a partir da Model
    public UsuarioResponseDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getFotoUsuario(),
                usuario.getPontuacaoUsuario(),
                usuario.getPontuacaoSoul()
        );
    }
}