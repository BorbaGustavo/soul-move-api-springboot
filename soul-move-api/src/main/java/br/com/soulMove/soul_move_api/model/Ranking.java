package br.com.soulMove.soul_move_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "T_SM_RANKING")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Ranking {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ranking_generator"
    )
    @SequenceGenerator(
            name = "ranking_generator",
            sequenceName = "SQ_SM_RANKING",
            allocationSize = 1
    )
    @Column(name = "id_ranking")
    private Long id;

    @Column(name = "posicao", nullable = false)
    private Integer posicao;

    @Column(name = "pt_total", nullable = false)
    private Integer pontuacaoTotal;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "T_SM_USUARIO_id_usuario",
            referencedColumnName = "id_usuario",
            nullable = false,
            unique = true
    )
    @ToString.Exclude
    private Usuario usuario;

    public Ranking(Integer posicao, Integer pontuacaoTotal, Usuario usuario) {
        this.posicao = posicao;
        this.pontuacaoTotal = pontuacaoTotal;
        this.usuario = usuario;
    }
}