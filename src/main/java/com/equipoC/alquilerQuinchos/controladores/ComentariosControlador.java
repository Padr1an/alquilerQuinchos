package com.equipoC.alquilerQuinchos.controladores;

import com.equipoC.alquilerQuinchos.entidades.Comentarios;
import com.equipoC.alquilerQuinchos.entidades.Inmueble;
import com.equipoC.alquilerQuinchos.entidades.Usuario;
import com.equipoC.alquilerQuinchos.excepciones.MiException;
import com.equipoC.alquilerQuinchos.servicios.ComentariosServicio;
import com.equipoC.alquilerQuinchos.servicios.InmuebleServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/comentarios")
public class ComentariosControlador {

    @Autowired
    private ComentariosServicio comentarioServicio;

    @Autowired
    private InmuebleServicio inmuebleServicio;
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROPIETARIO', 'ROLE_ADMIN')")
    @GetMapping("/registro/{idInm}")
    public String Comentario(HttpSession session, ModelMap modelo, @PathVariable("idInm") Long idInm) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Inmueble inmueble = inmuebleServicio.getOne(idInm);
        modelo.addAttribute("inmueble", inmueble);
        modelo.addAttribute("usuario", usuario);

        return "formulario_comentarios.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROPIETARIO')")
    @PostMapping("/registro")
    public String registroComentario(@RequestParam String idCliente, @RequestParam Long id, @RequestParam int calificacion, @RequestParam String comentario,
                                     @ModelAttribute("fechaAlta") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaComent, @RequestParam List<MultipartFile> archivo, ModelMap modelo ) {

        try {

            comentarioServicio.crearComentario(idCliente, id, comentario, calificacion, fechaComent, archivo);
            modelo.put("excelente", "su comentario ha sido publicado satisfactoriamente");
            return "redirect:/inicio";


        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());


            return "formulario_comentarios.html";

        }

    }

    @GetMapping("/listarComentario")

    public String listarComentarios(ModelMap modelo) {

        List<Comentarios> comentarios = comentarioServicio.listarComentarios();

        modelo.addAttribute("comentarios", comentarios);

        return "formularioComentario.html";

    }

    @GetMapping("/listarComentario2")
    public String listarComentariosDesc(ModelMap modelo) {

        List<Comentarios> comentarios = comentarioServicio.listarComentariosPorCalificacionDesc();

        modelo.addAttribute("comentarios", comentarios);

        return "formularioComentario.html";

    }

    @GetMapping("/modificar/{id}")
    public String modificarComentario(@PathVariable String id, ModelMap modelo) {

        modelo.put("comentario", comentarioServicio.getOne(id));

        List<Comentarios> comentario = comentarioServicio.listarComentarios();
        modelo.addAttribute("comentario", comentario);

        return "formularioModificarComentario.html";

    }

    @PostMapping("/modificar/{id}")
    public String modificarComentario(@PathVariable String id, String idCliente, String comentario, List<MultipartFile> Fotos, int calificacion, ModelMap modelo) {
        try {

            List<Comentarios> comentarios = comentarioServicio.listarComentarios();
            modelo.addAttribute("comentario", comentarios);

            comentarioServicio.modificarComentario(id, idCliente, comentario, calificacion, Fotos);

            return "redirect:../listarComentario";

        } catch (MiException ex) {

            List<Comentarios> comentarios = comentarioServicio.listarComentarios();
            modelo.addAttribute("error", ex.getMessage());

            return "formularioModificarComentario.html";

        }
    }

    @GetMapping("/eliminar/{id}")

    public String eliminarComentario(@PathVariable String id, ModelMap modelo) {

        modelo.put("eliminar", comentarioServicio.getOne(id));

        return "eliminarComentario.html";

    }

}

