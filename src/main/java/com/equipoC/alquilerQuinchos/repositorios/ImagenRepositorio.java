
package com.equipoC.alquilerQuinchos.repositorios;


import com.equipoC.alquilerQuinchos.entidades.Imagen;

import java.util.List;
import java.util.Optional;

import com.equipoC.alquilerQuinchos.entidades.Inmueble;
import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen, String>{

    public Optional<Imagen> findById(Long idImagen);


    @Query("SELECT i FROM Imagen i WHERE i.inmueble.id = :id ")
    public List<Imagen> buscarImagenesPorIdDeInmb(@Param("id") Long id);

    @Query("SELECT i FROM Imagen i WHERE i.comentarios.id = :id ")
    public Imagen buscarImagenesPorIdDeComent(@Param("id") String id);
    
    
}