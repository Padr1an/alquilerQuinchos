
package com.equipoC.alquilerQuinchos.repositorios;

import com.equipoC.alquilerQuinchos.entidades.Comentarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentariosRepositorio extends JpaRepository<Comentarios, String> {

    @Query("SELECT c FROM Comentarios c ORDER BY c.calificacion DESC")
    List<Comentarios> findAllOrderByCalificacionDesc();
    @Query("SELECT c FROM Comentarios c WHERE c.inmueble.id = :id ")
    List<Comentarios> buscarComentariosPorIdInm(@Param("id") Long id);


}
