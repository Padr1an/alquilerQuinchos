package com.equipoC.alquilerQuinchos.entidades;

import javax.persistence.*;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@ToString(onlyExplicitlyIncluded = true)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reserva {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Temporal(TemporalType.DATE)
    private Date fechaAlta;
    
    @Temporal(TemporalType.DATE)
    private Date fechaBaja;

    @ManyToOne
    private Inmueble inmueble;
    
    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Calendario calendario;

    
}
