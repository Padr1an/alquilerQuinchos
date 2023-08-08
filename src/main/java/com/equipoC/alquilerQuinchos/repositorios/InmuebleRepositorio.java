package com.equipoC.alquilerQuinchos.repositorios;

import com.equipoC.alquilerQuinchos.entidades.Inmueble;
import com.equipoC.alquilerQuinchos.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InmuebleRepositorio extends JpaRepository<Inmueble, Long> {

    @Query("SELECT i FROM Inmueble i WHERE i.id = :id ")
    public Inmueble buscarPorId(@Param("id") Long id);

    @Query("SELECT i FROM Inmueble i WHERE" +
    " CONCAT(i.nombre, i.ubicacion, i.cochera, i.parrilla, i.parrilla)" + "LIKE %?1%")
    public List<Inmueble> findAll(@Param("search") String search);
    Inmueble findByNombre(String nombre);

    List<Inmueble> findByUserProp(Usuario usuario);

    @Query("SELECT i FROM Inmueble i WHERE i.userProp.id = :id ")
    List<Inmueble> buscarPorUserId(@Param("id") String id);

}
