package br.com.soulMove.soul_move_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_SM_USUARIO")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "usuario_generator"
    )
    @SequenceGenerator(
            name = "usuario_generator",
            sequenceName = "SQ_SM_USUARIO",
            allocationSize = 1
    )
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "email", nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @Column(name = "foto_usuario", length = 500)
    private String fotoUsuario;

    @Column(name = "pt_usuario", nullable = false)
    private Integer pontuacaoUsuario = 0;

    @Column(name = "pt_soul", nullable = false)
    private Integer pontuacaoSoul = 0;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(
            name = "T_SM_RANKING_id_ranking",
            referencedColumnName = "id_ranking",
            unique = true
    )
    @ToString.Exclude
    private Ranking ranking;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<UsuarioAtividade> usuarioAtividade = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<UsuarioRecompensa> usuarioRecompensa = new ArrayList<>();

    // Construtor principal
    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.pontuacaoUsuario = 0;
        this.pontuacaoSoul = 0;
    }

    // Regras de negócio da classe

    public void adicionarPontos(Integer pontos) {
        if (pontos != null && pontos > 0) {
            this.pontuacaoUsuario += pontos;
            this.pontuacaoSoul += pontos; // Atualiza tanto o total quanto o saldo trocável
        }
    }

    /** Resgate: debita só o saldo trocável (pt_soul). pt_usuario (ranking) nunca diminui. */
    public void debitarPontosSoul(int custo) {
        if (custo <= 0) {
            throw new IllegalArgumentException("O custo deve ser maior que zero.");
        }
        if (this.pontuacaoSoul < custo) {
            throw new IllegalStateException("Saldo de pt_soul insuficiente.");
        }
        this.pontuacaoSoul -= custo;
    }

    public boolean temPontosParaResgatar(int custoRecompensa) {
        return custoRecompensa >= 0 && this.pontuacaoSoul >= custoRecompensa;
    }

    @Override
    public String toString() {
        return String.format(
                "Usuario{id=%d, nome='%s', pts=%d, soul=%d}",
                id,
                nome,
                pontuacaoUsuario,
                pontuacaoSoul
        );
    }
}