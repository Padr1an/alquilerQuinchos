package com.equipoC.alquilerQuinchos.controladores;

import com.equipoC.alquilerQuinchos.Enumeraciones.Rol;
import com.equipoC.alquilerQuinchos.entidades.Inmueble;
import com.equipoC.alquilerQuinchos.entidades.Reserva;
import com.equipoC.alquilerQuinchos.entidades.Usuario;
import com.equipoC.alquilerQuinchos.excepciones.MiException;
import com.equipoC.alquilerQuinchos.repositorios.ImagenRepositorio;
import com.equipoC.alquilerQuinchos.servicios.InmuebleServicio;
import com.equipoC.alquilerQuinchos.servicios.ReservaServicio;
import com.equipoC.alquilerQuinchos.servicios.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ReservaServicio reservaServicio;

    @Autowired
    private InmuebleServicio inmuebleServicio;

    @GetMapping("/")
    public String index(ModelMap modelo, @RequestParam(value = "search", required = false) String search, @RequestParam(value = "parrilla", required = false) String parrilla, @RequestParam(value = "pileta", required = false) String pileta, @RequestParam(value = "cochera", required = false) String cochera) {
        //String busqueda=search.concat(" "+parrilla).concat(pileta).concat(cochera);

        List<Inmueble> listaInmuebles = inmuebleServicio.listarInmueblesPorBusquedaPersonalizada(search);
        modelo.addAttribute("x", listaInmuebles);

        return "index.html";
    }



    @GetMapping("/nosotros")
    public String nosotros() {
        return "nosotros.html";
    }

    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo, @RequestParam(value = "search", required = false) String search) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        if (usuario != null) {
            modelo.addAttribute("usuario", usuario);
            List<Inmueble> listaInmuebles = inmuebleServicio.listarInmueblesPorBusquedaPersonalizada(search);
            modelo.addAttribute("x", listaInmuebles);
        }

        List<Reserva> misReservas = reservaServicio.listarReservasUsuario(usuario);
        modelo.addAttribute("misReservas", misReservas);

        boolean esPropietario = usuario != null && usuario.getRol() == Rol.PROPIETARIO;
        modelo.addAttribute("esPropietario", esPropietario);

        boolean esCliente = usuario != null && usuario.getRol() == Rol.CLIENTE;
        modelo.addAttribute("esCliente", esCliente);

        boolean esAdmin = usuario != null && usuario.getRol() == Rol.ADMIN;
        modelo.addAttribute("esAdmin", esAdmin);

        if (esAdmin) {
            List<Usuario> usuarios = usuarioServicio.listarUsuarios();
            modelo.addAttribute("usuarios", usuarios);
        }

        return "inicio.html";
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String username, @RequestParam String password,
                           @RequestParam String password2, @RequestParam String nombre,
                           @RequestParam String email, @RequestParam String telefono,
                           @RequestParam("rol") String rol, MultipartFile archivo, ModelMap modelo) throws MiException {

        try {

            usuarioServicio.crearUsuario(username, password, password2, nombre, email, telefono, rol, archivo);

            modelo.put("exito", "Usuario registrado correctamente!");

            return "redirect:/";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/";
        }

    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "Usuario o Contraseña invalidos!");
        }
        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROPIETARIO', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuario);
        System.out.println(usuario.getImagen().getId());
        return "usuario_modificar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROPIETARIO', 'ROLE_ADMIN')")
    @PostMapping("/perfil")
    public String actualizar(@RequestParam String id,@RequestParam String username,@RequestParam String password,
                             @RequestParam String nombre, @RequestParam String email, @RequestParam String telefono,
                             @RequestParam String rol, @RequestParam(required = false) MultipartFile archivo,
                             HttpSession session, ModelMap modelo) {
        try {
            Usuario usuarioSession = (Usuario) session.getAttribute("usuariosession");
            usuarioServicio.modificarUsuario(id, username, password, nombre, email, telefono, rol, archivo);

            usuarioSession = usuarioServicio.getOne(usuarioSession.getId());
            session.setAttribute("usuariosession", usuarioSession);

            return "redirect:/";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:../";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")

    @PostMapping("/perfil/eliminar/{id}")
    public String eliminarUsuario(@PathVariable String id, ModelMap modelo) throws MiException {

        usuarioServicio.borrarUsuarioPorId(id);
        modelo.put("exito", "Usuario eliminado exitosamente");
        return "redirect:/inicio";

    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROPIETARIO', 'ROLE_ADMIN')")


    @GetMapping("/mis_reservas")
    public String misReservas(ModelMap model, HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuariosession");

        List<Reserva> misReservas = reservaServicio.listarReservas(usuarioLogueado.getId());
        model.addAttribute("misReservas", misReservas);
        model.addAttribute("cliente", usuarioLogueado);
        return "mis_reservas.html";
    }

}

