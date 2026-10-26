package br.com.soulMove.soul_move_api.repository;

import br.com.soulMove.soul_move_api.model.Recompensa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecompensaRepository extends JpaRepository<Recompensa, Long> {

    List<Recompensa> findAllByOrderByValorAsc();
}