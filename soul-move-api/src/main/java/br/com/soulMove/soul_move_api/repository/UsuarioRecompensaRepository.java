package br.com.soulMove.soul_move_api.repository;

import br.com.soulMove.soul_move_api.model.UsuarioRecompensa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRecompensaRepository extends JpaRepository<UsuarioRecompensa, Long> {


    List<UsuarioRecompensa> findByUsuarioIdOrderByDataRecebimentoDescIdDesc(Long usuarioId);
}
