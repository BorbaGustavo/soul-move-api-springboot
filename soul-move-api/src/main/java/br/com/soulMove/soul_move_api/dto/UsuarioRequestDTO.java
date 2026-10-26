package br.com.soulMove.soul_move_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 120, message = "O nome não pode exceder 120 caracteres.")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um e-mail válido.")
        @Size(max = 120, message = "O e-mail não pode exceder 120 caracteres.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, max = 255, message = "A senha deve ter no mínimo 6 caracteres.")
        String senha,

        String fotoUsuario
) {}