package com.equipoC.alquilerQuinchos.servicios;

import com.equipoC.alquilerQuinchos.entidades.Calendario;
import com.equipoC.alquilerQuinchos.entidades.Inmueble;
import com.equipoC.alquilerQuinchos.entidades.Reserva;
import com.equipoC.alquilerQuinchos.entidades.Usuario;
import com.equipoC.alquilerQuinchos.excepciones.MiException;
import com.equipoC.alquilerQuinchos.repositorios.CalendarioRepositorio;
import com.equipoC.alquilerQuinchos.repositorios.InmuebleRepositorio;
import com.equipoC.alquilerQuinchos.repositorios.ReservaRepositorio;
import com.equipoC.alquilerQuinchos.repositorios.UsuarioRepositorio;

import java.util.*;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaServicio {

    @Autowired
    InmuebleRepositorio inmuebleRepositorio;
    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    @Autowired
    ReservaRepositorio reservaRepositorio;
    @Autowired
    private CalendarioRepositorio calendarioRepositorio;

    @Transactional
    public void crearReserva(Date fechaAlta, Date fechaBaja, Long idInmueble, String idCliente) throws MiException {
        validar(fechaAlta, fechaBaja);

        Inmueble inmueble = inmuebleRepositorio.buscarPorId(idInmueble);
        Usuario usuario = usuarioRepositorio.buscarPorId(idCliente);
        Reserva reserva = new Reserva();

        reserva.setInmueble(inmueble);
        reserva.setFechaAlta(fechaAlta);
        reserva.setFechaBaja(fechaBaja);
        reserva.setUsuario(usuario);
        reserva.setCalendario(inmueble.getCalendarioInmueble());

        reservaRepositorio.save(reserva);
        inmueble.getCalendarioInmueble().getReserva().add(reserva);

    }

    @Transactional
    public Reserva modificarReserva(Long id, Date fechaAlta, Date fechaBaja) throws MiException {
        validar(fechaAlta, fechaBaja);
        Optional<Reserva> respuesta = reservaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Reserva reservaExistente = respuesta.get();
            reservaExistente.setFechaAlta(fechaAlta);
            reservaExistente.setFechaBaja(fechaBaja);
            return reservaRepositorio.save(reservaExistente);
        } else {
            throw new MiException("No se encontró la reserva con el ID: " + id);
        }
    }

    @Transactional
    public void eliminarReserva(Long id) throws MiException {

        Optional<Reserva> respuesta = reservaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            try {
                reservaRepositorio.delete(respuesta.get());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public List<Reserva> listarReservasPorIdInm(Long idInm) {
        List<Reserva> reservaList = new ArrayList();

        reservaList = reservaRepositorio.buscarPorIdInmueble(idInm);
        return reservaList;
    }

    public List<Reserva> listarReservas(String id) {
        return reservaRepositorio.buscarPorIdCliente(id);
    }

    public List<Reserva> listarReservasUsuario(Usuario usuario) {
        return reservaRepositorio.findByUsuario(usuario);
    }

    public Reserva getOne(Long id) {
        return reservaRepositorio.getOne(id);
    }

    @Transactional
    public void eliminarReservasPorUsuarioId(String usuarioId) {
        Usuario usuario = usuarioRepositorio.getOne(usuarioId);
        List<Reserva> reservas = reservaRepositorio.findByUsuario(usuario);
        for (Reserva reserva : reservas) {
            reservaRepositorio.delete(reserva);
        }
    }

    private void validar(Date alta, Date baja) throws MiException {

        if (alta == null) {
            throw new MiException("Debe seleccionar una fecha de reserva disponible");
        }
        if (baja == null) {
            throw new MiException("Debe seleccionar una fecha de reserva disponible");
        }
    }

}
