package br.com.soulMove.soul_move_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequestDTO(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 120, message = "O nome não pode exceder 120 caracteres.")
        String nome,

        String fotoUsuario
) {
}
