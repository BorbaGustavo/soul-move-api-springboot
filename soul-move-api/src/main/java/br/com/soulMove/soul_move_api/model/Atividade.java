package br.com.soulMove.soul_move_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "T_SM_ATIVIDADE")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Atividade {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "atividade_generator"
    )
    @SequenceGenerator(
            name = "atividade_generator",
            sequenceName = "SQ_SM_ATIVIDADE",
            allocationSize = 1
    )
    @Column(name = "id_atividade")
    private Long id;

    @Column(name = "nm_atividade", nullable = false, length = 60)
    private String nome;

    @Column(name = "dc_atividade", nullable = false, length = 1000)
    private String descricao;

    @Column(name = "validacao", nullable = false, length = 50)
    private String validacao;

    @Column(name = "pt_atividade", nullable = false)
    private Integer pontuacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tp_atividade", nullable = false, length = 30)
    private TiposAtividade tipoAtividade;

    public Atividade(String nome, String descricao, String validacao, Integer pontuacao, TiposAtividade tipoAtividade) {
        this.nome = nome;
        this.descricao = descricao;
        this.validacao = validacao;
        this.pontuacao = pontuacao;
        this.tipoAtividade = tipoAtividade;
    }

    public boolean validarAtividade() {
        return this.validacao != null && !this.validacao.isBlank();
    }
}