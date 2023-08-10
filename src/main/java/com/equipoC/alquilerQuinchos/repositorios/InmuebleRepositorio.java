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

    @Query(
            value = "SELECT * FROM inmueble WHERE inmueble.nombre LIKE %:search% OR inmueble.ubicacion LIKE %:search% AND inmueble.pileta=1 AND inmueble.parrilla=1 AND inmueble.cochera=1",
            nativeQuery = true
    )
    public abstract List<Inmueble> findAllTodos(@Param("search") String search);

    @Query(
            value = "SELECT * FROM inmueble WHERE inmueble.nombre LIKE %:search% OR inmueble.ubicacion LIKE %:search%",
            nativeQuery = true
    )
    public abstract List<Inmueble> findAllSearch(@Param("search") String search);

    @Query(
            value = "SELECT * FROM inmueble WHERE inmueble.nombre LIKE %:search% OR inmueble.ubicacion LIKE %:search% AND inmueble.pileta=1",
            nativeQuery = true
    )
    public abstract List<Inmueble> findAllPile(@Param("search") String search);

    @Query(
            value = "SELECT * FROM inmueble WHERE inmueble.nombre LIKE %:search% OR inmueble.ubicacion LIKE %:search% AND inmueble.pileta=1 AND inmueble.parrilla=1",
            nativeQuery = true
    )
    public List<Inmueble> findAllPileParri(@Param("search") String search);

    @Query(
            value = "SELECT * FROM inmueble WHERE inmueble.nombre LIKE %:search% OR inmueble.ubicacion LIKE %:search% AND inmueble.pileta=1 AND inmueble.cochera=1",
            nativeQuery = true
    )
    public List<Inmueble> findAllPileCoche(@Param("search") String search);

    @Query(
            value = "SELECT * FROM inmueble WHERE inmueble.nombre LIKE %:search% OR inmueble.ubicacion LIKE %:search% AND inmueble.parrilla=1",
            nativeQuery = true
    )
    public List<Inmueble> findAllParri(@Param("search") String search);

    @Query(
            value = "SELECT * FROM inmueble WHERE inmueble.nombre OR inmueble.ubicacion LIKE %:search% AND inmueble.parrilla=1 AND inmueble.cochera=1",
            nativeQuery = true
    )
    public List<Inmueble> findAllParriCoche(@Param("search") String search);

    @Query(
            value = "SELECT * FROM inmueble WHERE inmueble.nombre LIKE %:search% OR inmueble.ubicacion LIKE %:search% AND inmueble.cochera=1",
            nativeQuery = true
    )
    public List<Inmueble> findAllCoche(@Param("search") String search);

    @Query(
            value = "SELECT * FROM inmueble WHERE inmueble.pileta=1 AND inmueble.parrilla=1 AND inmueble.cochera=1",
            nativeQuery = true
    )
    public abstract List<Inmueble> findAllTodosNull(@Param("search") String search);

    @Query(
            value = "SELECT * FROM inmueble WHERE inmueble.pileta=1",
            nativeQuery = true
    )
    public abstract List<Inmueble> findAllPileNull(@Param("search") String search);

    @Query(
            value = "SELECT * FROM inmueble WHERE inmueble.pileta=1 AND inmueble.parrilla=1",
            nativeQuery = true
    )
    public List<Inmueble> findAllPileParriNull(@Param("search") String search);

    @Query(
            value = "SELECT * FROM inmueble WHERE inmueble.pileta=1 AND inmueble.cochera=1",
            nativeQuery = true
    )
    public List<Inmueble> findAllPileCocheNull(@Param("search") String search);

    @Query(
            value = "SELECT * FROM inmueble WHERE inmueble.parrilla=1",
            nativeQuery = true
    )
    public List<Inmueble> findAllParriNull(@Param("search") String search);

    @Query(
            value = "SELECT * FROM inmueble WHERE inmueble.parrilla=1 AND inmueble.cochera=1",
            nativeQuery = true
    )
    public List<Inmueble> findAllParriCocheNull(@Param("search") String search);

    @Query(
            value = "SELECT * FROM inmueble WHERE inmueble.cochera=1",
            nativeQuery = true
    )
    public List<Inmueble> findAllCocheNull(@Param("search") String search);

    Inmueble findByNombre(String nombre);

    List<Inmueble> findByUserProp(Usuario usuario);

    @Query("SELECT i FROM Inmueble i WHERE i.userProp.id = :id ")
    List<Inmueble> buscarPorUserId(@Param("id") String id);

}
