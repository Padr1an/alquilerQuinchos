package com.equipoC.alquilerQuinchos.repositorios;

import com.equipoC.alquilerQuinchos.entidades.Reserva;
import com.equipoC.alquilerQuinchos.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepositorio extends JpaRepository<Reserva, Long> {

    @Query("SELECT r FROM Reserva r WHERE r.id = :id")
    public Reserva buscarPorId(@Param("id") String id);
    @Query("SELECT r FROM Reserva r WHERE r.usuario.id = :id")
    public List<Reserva> buscarPorIdCliente(@Param("id") String id);
    List<Reserva> findByUsuario(Usuario usuario);
    @Query("SELECT r FROM Reserva r WHERE r.inmueble.id = :id")
    public List<Reserva> buscarPorIdInmueble(@Param("id") Long id);
}
