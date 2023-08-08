package com.equipoC.alquilerQuinchos.controladores;

import com.equipoC.alquilerQuinchos.entidades.Inmueble;
import com.equipoC.alquilerQuinchos.entidades.Reserva;
import com.equipoC.alquilerQuinchos.entidades.Usuario;
import com.equipoC.alquilerQuinchos.excepciones.MiException;
import com.equipoC.alquilerQuinchos.repositorios.InmuebleRepositorio;
import com.equipoC.alquilerQuinchos.repositorios.ReservaRepositorio;
import com.equipoC.alquilerQuinchos.servicios.ReservaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Controller
@RequestMapping("/reserva")
public class ReservaControlador {

    @Autowired
    private ReservaRepositorio reservaRepositorio;
    @Autowired
    private ReservaServicio reservaServicio;
    @Autowired
    private InmuebleRepositorio inmuebleRepositorio;
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROPIETARIO', 'ROLE_ADMIN')")
    @GetMapping("/alta/{id}")
    public String crearReserva(HttpSession session,@PathVariable("id") Long idInmueble, ModelMap modelo) throws ParseException {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        Inmueble inmueble = inmuebleRepositorio.buscarPorId(idInmueble);

        modelo.addAttribute("cliente", logueado);
        modelo.addAttribute("idInmueble", idInmueble);


        ArrayList <Date> alta = new ArrayList<>();
        ArrayList <Date> baja = new ArrayList<>();
        for ( Reserva lista : inmueble.getReserva()) {
            Date entrada = new Date();

            SimpleDateFormat formato_DMY = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formato_YMD = new SimpleDateFormat("yyyy/MM/dd");
            String dmy1 = null;

            System.out.println(lista.getFechaAlta());
            dmy1= formato_YMD.format(lista.getFechaAlta());
            entrada = formato_DMY.parse(dmy1);

            alta.add(entrada);
        }
        for ( Reserva lista : inmueble.getReserva()) {
            Date salida = new Date();
            SimpleDateFormat formato_DMY = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formato_YMD = new SimpleDateFormat("yyyy/MM/dd");

            String dmy2 = null;
            System.out.println(lista.getFechaAlta());
            dmy2= formato_YMD.format(lista.getFechaBaja());
            //System.out.println(dmy1);
            salida = formato_DMY.parse(dmy2);
            //System.out.println(entrada);
            dmy2 = formato_YMD.format(lista.getFechaBaja());
            salida = formato_DMY.parse(dmy2);

            baja.add(salida);
        }

        modelo.addAttribute("inmu" , alta);
        return "crear_reserva.html";
    }
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROPIETARIO', 'ROLE_ADMIN')")
    @PostMapping("/alta")
    public String reserva(@RequestParam Long idInmueble, @RequestParam String fechaAlta,
                          @RequestParam String fechaBaja, @RequestParam String idCliente, HttpSession session, ModelMap modelo) throws MiException, ParseException {

        Usuario clienteSession = (Usuario) session.getAttribute("clientesession");

        Date entrada;
        Date salida = new Date();
        SimpleDateFormat formato_YMD = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat formato_DMY = new SimpleDateFormat("dd/MM/yyyy");

        entrada = formato_DMY.parse(fechaAlta);
        fechaAlta = formato_YMD.format(entrada);
        Date in = formato_YMD.parse(fechaAlta);

        salida = formato_DMY.parse(fechaBaja);
        fechaBaja = formato_YMD.format(salida);
        Date out = formato_YMD.parse(fechaBaja);

        try{
            reservaServicio.crearReserva(in,out, idInmueble, idCliente);
            modelo.put("exito", "La reserva fue cargada correctamente");
            return "redirect:../../inicio";
        } catch (MiException e) {

            modelo.put("error", e.getMessage());
            return "redirect:/inicio";
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROPIETARIO', 'ROLE_ADMIN')")
    @GetMapping("/modificar/{id}")
    public String mostrarFormularioModificarReserva(@PathVariable Long id, ModelMap modelo) {
        Reserva reserva = reservaServicio.getOne(id);
        modelo.addAttribute("reserva", reserva);
        return "reserva_modificar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROPIETARIO', 'ROLE_ADMIN')")
    @PostMapping("/modificar/{id}")
    public String actualizarReserva(@PathVariable Long id,
                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaAlta,
                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaBaja,
                                    ModelMap modelo) {

        try {
            reservaServicio.modificarReserva(id, fechaAlta, fechaBaja);
            modelo.put("exito", "La reserva fue modificada corectamente");
            return "redirect:../../inicio";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            return "modificar_reserva.html";
        }
    }
    @PostMapping("/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id) throws MiException {
        reservaServicio.eliminarReserva(id);
        return "redirect:/inicio";
    }
}