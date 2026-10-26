package br.com.soulMove.soul_move_api.controller;

import br.com.soulMove.soul_move_api.dto.AtividadeRequestDTO;
import br.com.soulMove.soul_move_api.dto.RegistroAtividadeRequestDTO;
import br.com.soulMove.soul_move_api.dto.UsuarioAtividadeResponseDTO;
import br.com.soulMove.soul_move_api.model.Atividade;
import br.com.soulMove.soul_move_api.model.TiposAtividade;
import br.com.soulMove.soul_move_api.model.UsuarioAtividade;
import br.com.soulMove.soul_move_api.service.AtividadeService;
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
@RequestMapping("/atividades")
@Tag(name = "Atividades", description = "Gerenciamento de atividades sustentáveis e registro de execuções")
public class AtividadeController {

    @Autowired
    private AtividadeService atividadeService;

    @GetMapping
    @Operation(summary = "Listar atividades disponíveis", description = "Retorna as atividades cadastradas. Use ?tipo=RECICLAGEM (por exemplo) para filtrar.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<Atividade>> listarTodas(@RequestParam(required = false) TiposAtividade tipo) {
        return ResponseEntity.ok(tipo == null
                ? atividadeService.listarAtividadesDisponiveis()
                : atividadeService.listarPorTipo(tipo));
    }

    @GetMapping("/tipos")
    @Operation(summary = "Listar tipos de atividade", description = "Valores do enum TiposAtividade, para o front montar filtros.")
    public ResponseEntity<List<TiposAtividade>> listarTipos() {
        return ResponseEntity.ok(atividadeService.listarTipos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar atividade por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Atividade encontrada"),
            @ApiResponse(responseCode = "404", description = "Atividade não encontrada")
    })
    public ResponseEntity<Atividade> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(atividadeService.buscarPorId(id));
    }

    @GetMapping("/historico/{usuarioId}")
    @Operation(summary = "Obter histórico de atividades", description = "Atividades já registradas pelo usuário, da mais recente para a mais antiga.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<List<UsuarioAtividadeResponseDTO>> obterHistorico(@PathVariable Long usuarioId) {
        List<UsuarioAtividadeResponseDTO> historico = atividadeService.obterHistoricoPorUsuario(usuarioId)
                .stream().map(UsuarioAtividadeResponseDTO::new).toList();
        return ResponseEntity.ok(historico);
    }

    @PostMapping
    @Operation(summary = "Cadastrar nova atividade", description = "Adiciona uma nova atividade sustentável ao catálogo do sistema.")
    @ApiResponse(responseCode = "201", description = "Atividade cadastrada com sucesso")
    public ResponseEntity<Atividade> cadastrarAtividade(@RequestBody @Valid AtividadeRequestDTO dto) {
        Atividade novaAtividade = new Atividade(
                dto.nome(), dto.descricao(), dto.validacao(), dto.pontuacao(), dto.tipoAtividade()
        );
        Atividade salva = atividadeService.salvar(novaAtividade);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PostMapping("/registrar")
    @Operation(summary = "Registrar realização de atividade", description = "Associa um usuário a uma atividade concluída, creditando os pontos correspondentes.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Atividade registrada e pontos creditados"),
            @ApiResponse(responseCode = "404", description = "Usuário ou atividade não existe"),
            @ApiResponse(responseCode = "409", description = "Limite diário desta atividade atingido")
    })
    public ResponseEntity<UsuarioAtividadeResponseDTO> registrarAtividade(
            @RequestBody @Valid RegistroAtividadeRequestDTO dto) {
        UsuarioAtividade registro = atividadeService.registrarAtividade(dto.usuarioId(), dto.atividadeId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioAtividadeResponseDTO(registro));
    }
}