package com.equipoC.alquilerQuinchos.entidades;

import com.equipoC.alquilerQuinchos.Enumeraciones.Rol;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;
import javax.persistence.*;

import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@ToString(onlyExplicitlyIncluded = true)
@Data
public class Usuario {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String username;
    private String password;
    private String nombre;
    private String email;
    private String telefono;
    private boolean estado; //activar o desactivar el user
    
    @Temporal(TemporalType.DATE)
    private Date alta;
    
    @OneToOne
    private Imagen imagen;
    
    @Enumerated(EnumType.STRING)
    private Rol rol;

    @OneToMany(mappedBy = "usuario")
    private Set<Reserva> reserva = new HashSet<>();

    @OneToMany(mappedBy = "userProp")
    private Set<Inmueble> inmueble = new HashSet<>();


}