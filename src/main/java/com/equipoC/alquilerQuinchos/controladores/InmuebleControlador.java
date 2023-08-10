package com.equipoC.alquilerQuinchos.controladores;

import com.equipoC.alquilerQuinchos.entidades.*;
import com.equipoC.alquilerQuinchos.excepciones.MiException;
import com.equipoC.alquilerQuinchos.repositorios.ComentariosRepositorio;
import com.equipoC.alquilerQuinchos.repositorios.ImagenRepositorio;
import com.equipoC.alquilerQuinchos.repositorios.InmuebleRepositorio;
import com.equipoC.alquilerQuinchos.servicios.InmuebleServicio;
import com.equipoC.alquilerQuinchos.servicios.ReservaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/inmueble")
public class InmuebleControlador {

    @Autowired
    private InmuebleRepositorio inmuebleRepositorio;
    @Autowired
    private ComentariosRepositorio comentariosRepositorio;
    @Autowired
    private InmuebleServicio inmuebleServicio;
    @Autowired
    private ReservaServicio reservaServicio;

    @Autowired
    private ImagenRepositorio imagenRepositorio;

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO', 'ROLE_ADMIN')")

    @GetMapping("/registrar")
    public String crearInmuebleHTML(ModelMap modelo) {
        modelo.addAttribute("titulo", "Crear Inmueble");
        return "crear_inmueble.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO', 'ROLE_ADMIN')")

    @PostMapping("/crear_inmueble")
    public String crearInmueble(@RequestParam String nombre, @RequestParam String ubicacion, @RequestParam(required = false) Boolean cochera, @RequestParam(required = false) Boolean parrilla,
            @RequestParam(required = false) Boolean pileta, @RequestParam Double precioBase, @RequestParam("archivosImagenes") List<MultipartFile> archivosImagenesList,
            ModelMap modelo, HttpSession session) {
        if (cochera == null) {
            cochera = false;
        }
        if (parrilla == null) {
            parrilla = false;
        }
        if (pileta == null) {
            pileta = false;
        }

        try {
            Double subtotal1 = (double) 0;
            Double subtotal2 = (double) 0;
            Double subtotal3 = (double) 0;

            if (cochera == true) {
                subtotal1 = precioBase * 0.1;
            } else if (pileta == true) {
                subtotal2 = precioBase * 0.1;
            } else if (parrilla == true) {
                subtotal3 = precioBase * 0.1;

            }

            Double precioFinal = subtotal1 + subtotal2 + subtotal3 + precioBase;

            Usuario usuarioPropietario = (Usuario) session.getAttribute("usuariosession");

            inmuebleServicio.crearInmueble(nombre, ubicacion, cochera, parrilla, pileta, precioFinal, precioBase, archivosImagenesList, usuarioPropietario.getId());

            modelo.addAttribute("mensaje", "Inmueble creado exitosamente.");

            return "redirect:/inicio";
        } catch (MiException ex) {
            modelo.addAttribute("error", "Error al crear el inmueble: " + ex.getMessage());
        }

        return "crear_inmueble.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO', 'ROLE_ADMIN')")

    @GetMapping("/mis_inmuebles")
    public String misInmuebles(ModelMap model, HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuariosession");

        List<Inmueble> misInmuebles = inmuebleServicio.listarInmueblesUsuario(usuarioLogueado.getId());
        model.addAttribute("misInmuebles", misInmuebles);
        model.addAttribute("cliente", usuarioLogueado);

       // List<Reserva> reservas = reservaServicio.listarReservas();
       // model.addAttribute("reservas", reservas);
     return "mis_inmuebles.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO', 'ROLE_ADMIN')")

    @GetMapping("/modificar/{id}")
    public String mostrarFormularioModificarInmueble(@PathVariable Long id, ModelMap modelo) {
        List<Imagen> imagen = imagenRepositorio.buscarImagenesPorIdDeInmb(id);

        Inmueble inmueble = inmuebleServicio.getOne(id);
        modelo.addAttribute("inmueble", inmueble);
        modelo.addAttribute("imagenes", imagen);
        return "inmueble_modificar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO', 'ROLE_ADMIN')")
    @PostMapping("/modificar/{id}")

    public String actualizarInmueble(@PathVariable Long id, @RequestParam(required = false) String nombre,
                                     @RequestParam(required = false) String ubicacion,
                                     @RequestParam(required = false) Boolean cochera,
                                     @RequestParam(required = false) Boolean parrilla,
                                     @RequestParam(required = false) Boolean pileta,
                                     @RequestParam(required = false) Double precioBase,
                                     @RequestParam(required = false) Double precioTotal,
                                     @RequestParam(value = "archivosImagenes", required = false) List<MultipartFile> archivosImagenes,
                                     ModelMap modelo, HttpServletRequest request) throws MiException {

        try {
            Inmueble inmueble = inmuebleServicio.modificarInmueble(id, nombre, ubicacion, cochera, parrilla, pileta, precioBase, precioTotal, archivosImagenes);
            modelo.addAttribute("inmueble", inmueble);
            modelo.put("exito", "Datos modificados con éxito");

        } catch (MiException ex) {
            return "redirect:../mis_inmuebles";
        }
        return "redirect:/inicio";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO', 'ROLE_ADMIN')")

    @PostMapping("/eliminar/{id}")
    public String eliminarInmueble(@PathVariable Long id) throws MiException {
        inmuebleServicio.eliminarInmueble(id);
        return "redirect:../../inicio";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO', 'ROLE_ADMIN','ROLE_CLIENTE')")

    @GetMapping("/detalles/{idImnueble}")
    public String inmuebleDetalles(@PathVariable("idImnueble") Long id, ModelMap modelo) {
        Inmueble inmueble = inmuebleRepositorio.buscarPorId(id);
        List<Comentarios> comentario = comentariosRepositorio.buscarComentariosPorIdInm(id);

        modelo.addAttribute("inmueble", inmueble);
        modelo.addAttribute("comentarios", comentario);

        return "detalle_inmueble.html";
    }

}
