package br.com.soulMove.soul_move_api.dto;

import jakarta.validation.constraints.NotNull;

public record ResgateRecompensaRequestDTO(
        @NotNull(message = "O ID do usuário é obrigatório.")
        Long usuarioId,

        @NotNull(message = "O ID da recompensa é obrigatório.")
        Long recompensaId
) {}