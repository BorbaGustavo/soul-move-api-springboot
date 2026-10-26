package br.com.soulMove.soul_move_api.controller;

import br.com.soulMove.soul_move_api.dto.LoginRequestDTO;
import br.com.soulMove.soul_move_api.dto.UsuarioRequestDTO;
import br.com.soulMove.soul_move_api.dto.UsuarioUpdateRequestDTO;
import br.com.soulMove.soul_move_api.dto.UsuarioResponseDTO;
import br.com.soulMove.soul_move_api.model.Usuario;
import br.com.soulMove.soul_move_api.service.UsuarioService;
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
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Gerenciamento de usuários, perfis, buscas e pontuações")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Cadastrar novo usuário", description = "Cria um novo usuário na plataforma com base nos dados informados.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail já cadastrado")
    })
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody @Valid UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario(dto.nome(), dto.email(), dto.senha());
        usuario.setFotoUsuario(dto.fotoUsuario());
        Usuario salvo = usuarioService.salvar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDTO(salvo));
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Confere e-mail e senha e devolve o usuário. O front guarda o id para as demais rotas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos")
    })
    public ResponseEntity<UsuarioResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        return ResponseEntity.ok(new UsuarioResponseDTO(usuarioService.login(dto.email(), dto.senha())));
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista com todos os usuários cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        List<UsuarioResponseDTO> lista = usuarioService.listarTodos()
                .stream().map(UsuarioResponseDTO::new).toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna os detalhes de um usuário específico através do ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
    }

    @GetMapping("/email")
    @Operation(summary = "Buscar usuário por e-mail", description = "Retorna o usuário correspondente ao e-mail informado por parâmetro.")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado")
    public ResponseEntity<UsuarioResponseDTO> buscarPorEmail(@RequestParam String email) {
        Usuario usuario = usuarioService.buscarPorEmail(email);
        return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
    }

    @GetMapping("/pesquisar")
    @Operation(summary = "Pesquisar usuários por nome", description = "Filtra usuários cujo nome contenha o termo pesquisado.")
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    public ResponseEntity<List<UsuarioResponseDTO>> pesquisarPorNome(@RequestParam String nome) {
        List<UsuarioResponseDTO> lista = usuarioService.pesquisarPorNome(nome)
                .stream().map(UsuarioResponseDTO::new).toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/top10")
    @Operation(summary = "Obter Top 10 pontuadores", description = "Retorna os 10 usuários com maior pontuação geral na plataforma.")
    @ApiResponse(responseCode = "200", description = "Ranking parcial retornado com sucesso")
    public ResponseEntity<List<UsuarioResponseDTO>> obterTop10() {
        List<UsuarioResponseDTO> top10 = usuarioService.obterTop10Pontuadores()
                .stream().map(UsuarioResponseDTO::new).toList();
        return ResponseEntity.ok(top10);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar perfil", description = "Altera nome e foto. E-mail e pontos não são editáveis.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil atualizado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id,
                                                        @RequestBody @Valid UsuarioUpdateRequestDTO dto) {
        return ResponseEntity.ok(new UsuarioResponseDTO(usuarioService.atualizarPerfil(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar usuário", description = "Remove um usuário do sistema com base no ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}