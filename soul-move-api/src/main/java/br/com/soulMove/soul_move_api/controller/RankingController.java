package br.com.soulMove.soul_move_api.controller;

import br.com.soulMove.soul_move_api.dto.RankingResponseDTO;
import br.com.soulMove.soul_move_api.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ranking")
@Tag(name = "Ranking", description = "Consulta do placar geral de pontuações da plataforma")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping
    @Operation(summary = "Obter ranking geral", description = "Classificação ordenada por pontuação. Use ?limite=10 para o Top 10.")
    @ApiResponse(responseCode = "200", description = "Ranking retornado com sucesso")
    public ResponseEntity<List<RankingResponseDTO>> obterRankingGeral(@RequestParam(required = false) Integer limite) {
        var stream = rankingService.obterRankingCompleto().stream();
        if (limite != null && limite > 0) {
            stream = stream.limit(limite);
        }
        return ResponseEntity.ok(stream.map(RankingResponseDTO::new).toList());
    }

    @GetMapping("/{usuarioId}")
    @Operation(summary = "Posição de um usuário no ranking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Posição retornada"),
            @ApiResponse(responseCode = "404", description = "Usuário sem ranking")
    })
    public ResponseEntity<RankingResponseDTO> obterPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(new RankingResponseDTO(rankingService.buscarPorUsuarioOuFalhar(usuarioId)));
    }
}