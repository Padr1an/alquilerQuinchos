package com.equipoC.alquilerQuinchos.repositorios;

import com.equipoC.alquilerQuinchos.Enumeraciones.Rol;
import com.equipoC.alquilerQuinchos.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

    @Query("SELECT u FROM Usuario u WHERE u.id = :id")
    public Usuario buscarPorId(@Param("id") String id);

    @Query("SELECT u FROM Usuario u WHERE u.username = :username")
    public Usuario buscarPorUsername(@Param("username") String username);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public String buscarPorEmaill(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarPorEmail(@Param("email") String email);

    public List<Usuario> findAllByRol(Rol rol);

    @Query(
            value = "SELECT estado FROM usuario WHERE usuario.email LIKE %:email%",
            nativeQuery = true
    )
    public Boolean buscarEstado(@Param("email") String email);

    @Query(
            value = "SELECT COUNT(username) FROM usuario WHERE usuario.username LIKE %:username%",
            nativeQuery = true
    )
    public int buscarUsernameRepetido(@Param("username") String username);

}
