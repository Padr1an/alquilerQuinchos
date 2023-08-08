package com.equipoC.alquilerQuinchos.servicios;

import com.equipoC.alquilerQuinchos.entidades.Calendario;
import com.equipoC.alquilerQuinchos.entidades.Reserva;
import com.equipoC.alquilerQuinchos.repositorios.CalendarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class CalendarioServicio {
    @Autowired
    private CalendarioRepositorio calendarioRepositorio;

    @Transactional
    public Calendario  crearCalendario(){

        Calendario calendario = new Calendario();
        Set<Reserva> listaReserva = new HashSet<>();
        calendario.setReserva(listaReserva);
        calendarioRepositorio.save(calendario);
        return calendario;
    }
}
