package br.com.soulMove.soul_move_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "T_SM_RECOMPENSA")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Recompensa {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "recompensa_generator"
    )
    @SequenceGenerator(
            name = "recompensa_generator",
            sequenceName = "SQ_SM_RECOMPENSA",
            allocationSize = 1
    )
    @Column(name = "id_recompensa")
    private Long id;

    @Column(name = "dc_recompensa", nullable = false, length = 1000)
    private String descricao;

    @Column(name = "valor", nullable = false)
    private Integer valor;

    public Recompensa(String descricao, Integer valor) {
        this.descricao = descricao;
        this.valor = valor;
    }

    public boolean podeReceberRecompensa(Integer pontuacaoSoul) {
        return pontuacaoSoul != null && pontuacaoSoul >= this.valor;
    }
}