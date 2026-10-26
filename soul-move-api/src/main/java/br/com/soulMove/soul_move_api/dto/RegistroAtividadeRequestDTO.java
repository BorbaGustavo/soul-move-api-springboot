package br.com.soulMove.soul_move_api.dto;

import jakarta.validation.constraints.NotNull;

public record RegistroAtividadeRequestDTO(
        @NotNull(message = "O ID do usuário é obrigatório.")
        Long usuarioId,

        @NotNull(message = "O ID da atividade é obrigatório.")
        Long atividadeId
) {}