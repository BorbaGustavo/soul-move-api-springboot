package br.com.soulMove.soul_move_api.dto;

import br.com.soulMove.soul_move_api.model.TiposAtividade;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtividadeRequestDTO(
        @NotBlank(message = "O nome da atividade é obrigatório.")
        String nome,

        @NotBlank(message = "A descrição é obrigatória.")
        String descricao,

        @NotBlank(message = "O critério de validação é obrigatório.")
        String validacao,

        @NotNull(message = "A pontuação é obrigatória.")
        @Min(value = 1, message = "A pontuação deve ser maior que zero.")
        Integer pontuacao,

        @NotNull(message = "O tipo de atividade é obrigatório.")
        TiposAtividade tipoAtividade
) {}