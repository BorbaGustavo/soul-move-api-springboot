package br.com.soulMove.soul_move_api.repository;

import br.com.soulMove.soul_move_api.model.Ranking;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {

    @Query("SELECT r FROM Ranking r JOIN FETCH r.usuario WHERE r.usuario.id = :usuarioId")
    Optional<Ranking> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT r FROM Ranking r JOIN FETCH r.usuario ORDER BY r.pontuacaoTotal DESC")
    List<Ranking> findAllByOrderByPontuacaoTotalDesc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Ranking r order by r.id asc")
    List<Ranking> findTodosParaRecalculo();
}