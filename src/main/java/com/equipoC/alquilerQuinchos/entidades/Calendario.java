package com.equipoC.alquilerQuinchos.entidades;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Calendario  {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private Calendar calendario = Calendar.getInstance();

    @OneToMany(mappedBy = "calendario")
    private Set<Reserva> reserva = new HashSet<>();

}
