package br.com.soulMove.soul_move_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecompensaRequestDTO(
        @NotBlank(message = "A descrição da recompensa não pode estar em branco")
        String descricao,

        @NotNull(message = "O valor em pontos é obrigatório")
        @Positive(message = "O valor em pontos deve ser maior que zero")
        Integer valor
) {}
