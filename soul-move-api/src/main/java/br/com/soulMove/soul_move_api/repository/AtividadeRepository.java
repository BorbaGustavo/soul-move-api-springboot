package br.com.soulMove.soul_move_api.repository;

import br.com.soulMove.soul_move_api.model.Atividade;
import br.com.soulMove.soul_move_api.model.TiposAtividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {

    List<Atividade> findByTipoAtividade(TiposAtividade tipoAtividade);
}
