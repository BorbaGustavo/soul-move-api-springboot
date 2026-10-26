package br.com.soulMove.soul_move_api.controller;

import br.com.soulMove.soul_move_api.dto.ResgateRecompensaRequestDTO;
import br.com.soulMove.soul_move_api.dto.UsuarioRecompensaResponseDTO;
import br.com.soulMove.soul_move_api.model.Recompensa;
import br.com.soulMove.soul_move_api.model.UsuarioRecompensa;
import br.com.soulMove.soul_move_api.service.RecompensaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recompensas")
@Tag(name = "Recompensas", description = "Catálogo de prêmios e gestão de resgates utilizando pontos Soul")
public class RecompensaController {

    @Autowired
    private RecompensaService recompensaService;

    @GetMapping
    @Operation(summary = "Listar recompensas disponíveis", description = "Retorna todas as recompensas que podem ser resgatadas com pontos.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<Recompensa>> listarTodas() {
        return ResponseEntity.ok(recompensaService.getRecompensasDisponiveis());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar recompensa por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recompensa encontrada"),
            @ApiResponse(responseCode = "404", description = "Recompensa não encontrada")
    })
    public ResponseEntity<Recompensa> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(recompensaService.buscarPorId(id));
    }

    @PostMapping("/resgatar")
    @Operation(summary = "Resgatar recompensa", description = "Debita os pontos Soul do usuário e gera o resgate de um prêmio selecionado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Recompensa resgatada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário ou recompensa não existe"),
            @ApiResponse(responseCode = "409", description = "Saldo de pontos insuficiente")
    })
    public ResponseEntity<UsuarioRecompensaResponseDTO> resgatarRecompensa(
            @RequestBody @Valid ResgateRecompensaRequestDTO dto) {
        UsuarioRecompensa resgate = recompensaService.resgatarRecompensa(dto.usuarioId(), dto.recompensaId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioRecompensaResponseDTO(resgate));
    }

    @GetMapping("/historico/{usuarioId}")
    @Operation(summary = "Obter histórico de resgates", description = "Lista todas as recompensas já resgatadas por um usuário específico.")
    @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso")
    public ResponseEntity<List<UsuarioRecompensaResponseDTO>> obterHistorico(@PathVariable Long usuarioId) {
        List<UsuarioRecompensaResponseDTO> historico = recompensaService.obterHistoricoPorUsuario(usuarioId)
                .stream().map(UsuarioRecompensaResponseDTO::new).toList();
        return ResponseEntity.ok(historico);
    }
}