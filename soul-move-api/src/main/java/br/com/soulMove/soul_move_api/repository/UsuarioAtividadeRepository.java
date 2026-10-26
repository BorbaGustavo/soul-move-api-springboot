package br.com.soulMove.soul_move_api.repository;

import br.com.soulMove.soul_move_api.model.UsuarioAtividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UsuarioAtividadeRepository extends JpaRepository<UsuarioAtividade, String> {
    List<UsuarioAtividade> findByUsuarioIdOrderByDataAtividadeDescIdDesc(Long usuarioId);

    @Query("""
           select count(ua) from UsuarioAtividade ua
            where ua.usuario.id = :usuarioId
              and ua.atividade.id = :atividadeId
              and ua.dataAtividade = :data
           """)
    long contarNoDia(@Param("usuarioId") Long usuarioId,
                     @Param("atividadeId") Long atividadeId,
                     @Param("data") LocalDate data);
}
