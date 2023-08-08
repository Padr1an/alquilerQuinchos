package com.equipoC.alquilerQuinchos.entidades;

import java.util.List;
import lombok.Data;


import javax.persistence.*;


import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Comentarios {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String comentario;
    private int calificacion;//Se agregó calif
    @Temporal(TemporalType.DATE)
    private Date fechaPublicacion; // agrego fecha de publicación del comentario

    @OneToMany(mappedBy = "comentarios")
    private List<Imagen> fotos = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario userCliente;//se agrega el id del usuario q comenta

    @ManyToOne(fetch = FetchType.LAZY)

    private Inmueble inmueble;

}
