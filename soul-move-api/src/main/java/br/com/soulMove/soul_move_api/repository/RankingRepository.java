package br.com.soulMove.soul_move_api.repository;

import br.com.soulMove.soul_move_api.model.Ranking;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {

    Optional<Ranking> findByUsuarioId(Long usuarioId);

    List<Ranking> findAllByOrderByPontuacaoTotalDesc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Ranking r order by r.id asc")
    List<Ranking> findTodosParaRecalculo();
}