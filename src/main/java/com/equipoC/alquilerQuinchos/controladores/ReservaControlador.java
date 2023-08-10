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
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    public String crearReserva(HttpSession session, @PathVariable("id") Long idInmueble, ModelMap modelo) throws ParseException {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        Inmueble inmueble = inmuebleRepositorio.buscarPorId(idInmueble);

        modelo.addAttribute("cliente", logueado);
        modelo.addAttribute("idInmueble", idInmueble);

        ArrayList<Date> alta = new ArrayList<>();
        for (Reserva lista : inmueble.getReserva()) {
            alta.add(lista.getFechaAlta());
        }

        ArrayList<Date> baja = new ArrayList<>();
        for (Reserva lista : inmueble.getReserva()) {
            baja.add(lista.getFechaBaja());
        }
        for (Date imp : alta) {
            System.out.println(imp);
        }
        modelo.addAttribute("bajas", baja);
        modelo.addAttribute("altas", alta);
        return "crear_reserva.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROPIETARIO', 'ROLE_ADMIN')")
    @PostMapping("/alta")
    public String reserva(@RequestParam Long idInmueble, @RequestParam String fechaAlta,
            @RequestParam String fechaBaja, @RequestParam String idCliente, HttpSession session, ModelMap modelo) throws MiException, ParseException {

        Usuario clienteSession = (Usuario) session.getAttribute("clientesession");
        System.out.println(fechaAlta);
        Date entrada = new Date();
        Date salida = new Date();
        SimpleDateFormat formato_YMD = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formato_DMY = new SimpleDateFormat("dd-MM-yyyy");

        try {
            entrada = formato_YMD.parse(fechaAlta);
            salida = formato_YMD.parse(fechaBaja);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        salida = formato_YMD.parse(fechaBaja);

        System.out.println(entrada);
        System.out.println(salida);
        try {
            reservaServicio.crearReserva(entrada, salida, idInmueble, idCliente);
            modelo.put("exito", "La reserva fue cargada correctamente");
            return "redirect:../../inicio";
        } catch (MiException e) {

            modelo.put("error", e.getMessage());
            return "redirect:/inicio";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROPIETARIO', 'ROLE_ADMIN')")
    @GetMapping("/modificar/{id}")
    public String mostrarFormularioModificarReserva(HttpSession session, @PathVariable("id") Long idInmueble, @PathVariable Long id, ModelMap modelo) {
                Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        Inmueble inmueble = inmuebleRepositorio.buscarPorId(idInmueble);

        modelo.addAttribute("cliente", logueado);
        modelo.addAttribute("idInmueble", idInmueble);

        ArrayList<Date> alta = new ArrayList<>();
        for (Reserva lista : inmueble.getReserva()) {
            alta.add(lista.getFechaAlta());
        }

        ArrayList<Date> baja = new ArrayList<>();
        for (Reserva lista : inmueble.getReserva()) {
            baja.add(lista.getFechaBaja());
        }
        for (Date imp : alta) {
            System.out.println(imp);
        }
        modelo.addAttribute("bajas", baja);
        modelo.addAttribute("altas", alta);
        
        Reserva reserva = reservaServicio.getOne(id);
        modelo.addAttribute("reserva", reserva);

        return "reserva_modificar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROPIETARIO', 'ROLE_ADMIN')")
    @PostMapping("/modificar/{id}")
    public String actualizarReserva(@PathVariable Long id,
            @RequestParam String fechaAlta,
            @RequestParam String fechaBaja,
            ModelMap modelo) throws ParseException {

        Date entrada = new Date();
        Date salida = new Date();
        SimpleDateFormat formato_YMD = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formato_DMY = new SimpleDateFormat("dd-MM-yyyy");

        try {
            entrada = formato_YMD.parse(fechaAlta);
            salida = formato_YMD.parse(fechaBaja);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        salida = formato_YMD.parse(fechaBaja);
        
        try {
            reservaServicio.modificarReserva(id, entrada, salida);
            modelo.put("exito", "La reserva fue modificada corectamente");
            return "redirect:../../inicio";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            return "modificar_reserva.html";
        }
    }

    @PostMapping("/eliminar/{id}")
    public RedirectView eliminarReserva(@PathVariable Long id, HttpServletRequest request, ModelMap modelo) throws MiException {
        try {
            reservaServicio.eliminarReserva(id);
        }catch (MiException e){
            modelo.put("error", e.getMessage());
        }
        String referer = request.getHeader("referer");

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(referer); // Redirige a la página anterior
        return redirectView;
    }
}
