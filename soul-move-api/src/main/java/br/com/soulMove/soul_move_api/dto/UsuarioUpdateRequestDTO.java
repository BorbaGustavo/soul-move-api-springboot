package br.com.soulMove.soul_move_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequestDTO(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 120, message = "O nome não pode exceder 120 caracteres.")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Formato de e-mail inválido.")
        String email,

        String fotoUsuario,

        @Size(min = 8, max = 100, message = "A senha deve ter no mínimo 8 caracteres.")
        String senha
) {
}
