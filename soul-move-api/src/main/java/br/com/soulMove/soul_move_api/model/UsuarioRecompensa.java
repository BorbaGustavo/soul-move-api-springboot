package br.com.soulMove.soul_move_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "T_SM_USUARIO_RECOMPENSA")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UsuarioRecompensa {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "usr_recomp_generator"
    )
    @SequenceGenerator(
            name = "usr_recomp_generator",
            sequenceName = "SQ_SM_USR_RECOMP",
            allocationSize = 1
    )
    @Column(name = "id_usuario_recompensa")
    private Long id;

    @Column(name = "dt_recebimento", nullable = false)
    private LocalDate dataRecebimento;

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
            name = "T_SM_RECOMPENSA_id_recompensa",
            referencedColumnName = "id_recompensa",
            nullable = false
    )
    @ToString.Exclude
    private Recompensa recompensa;

    public UsuarioRecompensa(LocalDate dataRecebimento, Usuario usuario, Recompensa recompensa) {
        this.dataRecebimento = dataRecebimento;
        this.usuario = usuario;
        this.recompensa = recompensa;
    }
}