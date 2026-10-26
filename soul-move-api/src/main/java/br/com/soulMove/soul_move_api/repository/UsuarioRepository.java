package br.com.soulMove.soul_move_api.repository;

import br.com.soulMove.soul_move_api.model.Usuario;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByEmail(String email);


    boolean existsByEmail(String email);


    List<Usuario> findByNomeContainingIgnoreCase(String nome);


    List<Usuario> findAllByOrderByPontuacaoUsuarioDesc();


    List<Usuario> findTop10ByOrderByPontuacaoUsuarioDesc();


    /** SELECT ... FOR UPDATE: cliques simultâneos não furam limite diário nem gastam o mesmo saldo 2x. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from Usuario u where u.id = :id")
    Optional<Usuario> findByIdForUpdate(@Param("id") Long id);
}
