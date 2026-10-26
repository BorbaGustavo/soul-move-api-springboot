package br.com.soulMove.soul_move_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "T_SM_USUARIO_ATIVIDADE")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UsuarioAtividade {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "usr_ativ_generator"
    )
    @SequenceGenerator(
            name = "usr_ativ_generator",
            sequenceName = "SQ_SM_USR_ATIV",
            allocationSize = 1
    )
    @Column(name = "id_usuario_atividade")
    private Long id;

    @Column(name = "dt_atividade", nullable = false)
    private LocalDate dataAtividade;

    @Column(name = "pt_recebida", nullable = false)
    private Integer pontosRecebida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "T_SM_USUARIO_id_usuario",
            referencedColumnName = "id_usuario",
            nullable = false
    )
    @ToString.Exclude
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "T_SM_ATIVIDADE_id_atividade",
            referencedColumnName = "id_atividade",
            nullable = false
    )
    @ToString.Exclude
    private Atividade atividade;

    public UsuarioAtividade(LocalDate dataAtividade, Integer pontosRecebida, Usuario usuario, Atividade atividade) {
        this.dataAtividade = dataAtividade;
        this.pontosRecebida = pontosRecebida;
        this.usuario = usuario;
        this.atividade = atividade;
    }
}