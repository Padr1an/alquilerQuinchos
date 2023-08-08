package com.equipoC.alquilerQuinchos.entidades;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@ToString(onlyExplicitlyIncluded = true)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Inmueble {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    
    private String ubicacion;

    private Boolean cochera;
    private Boolean parrilla;
    private Boolean pileta;
    private Double precioBase;
    private Double PrecioTotal;

    @OneToMany(mappedBy = "inmueble")
    private Set<Reserva> reserva = new HashSet<>();

    @OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagen> imagenInmueble = new ArrayList<>();
    
    @OneToMany(mappedBy = "inmueble")
    private Set<Comentarios> comentarios = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario userProp;

    @OneToOne
    private Calendario calendarioInmueble;

    
}
