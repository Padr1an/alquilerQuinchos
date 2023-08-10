package com.equipoC.alquilerQuinchos.servicios;

import com.equipoC.alquilerQuinchos.Enumeraciones.Rol;
import com.equipoC.alquilerQuinchos.entidades.Imagen;
import com.equipoC.alquilerQuinchos.entidades.Usuario;
import com.equipoC.alquilerQuinchos.excepciones.MiException;
import com.equipoC.alquilerQuinchos.repositorios.ImagenRepositorio;
import com.equipoC.alquilerQuinchos.repositorios.UsuarioRepositorio;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenRepositorio imagenRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void crearUsuario(String username, String password, String password2, String nombre, String email, String telefono, String rol, MultipartFile archivo) throws MiException {

        validarCrearUsuario(username, password, password2, nombre, email, telefono, rol);
        System.out.println("hola ggiitt");
        Usuario usuario = new Usuario();

        usuario.setUsername(username);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);
        usuario.setEstado(true);

        if ("CLIENTE".equalsIgnoreCase(rol)) {
            usuario.setRol(Rol.CLIENTE);
        } else if ("PROPIETARIO".equalsIgnoreCase(rol)) {
            usuario.setRol(Rol.PROPIETARIO);
        } else {
            usuario.setRol(Rol.ADMIN);
        }

        Imagen imagen = imagenServicio.guardar(archivo);

        usuario.setImagen(imagen);

        usuario.setAlta(new Date());

        usuarioRepositorio.save(usuario);

    }

    @Transactional
    public void modificarUsuario(String id, String username, String password, String nombre, String email, String telefono, String rol, MultipartFile archivo) throws MiException {

        validarModificarUsuario(username, password, nombre, telefono, rol);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            usuario.setUsername(username);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setTelefono(telefono);

            if ("CLIENTE".equalsIgnoreCase(rol)) {
                usuario.setRol(Rol.CLIENTE);
            } else if ("PROPIETARIO".equalsIgnoreCase(rol)) {
                usuario.setRol(Rol.PROPIETARIO);
            }

            String idImagen = null;

            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            usuario.setImagen(imagen);

            usuarioRepositorio.save(usuario);
        }
    }

    @Transactional()
    public List<Usuario> listarUsuarios() {

        List<Usuario> usuarios = new ArrayList();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }

    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }

    private void validarCrearUsuario(String username, String password, String password2, String nombre, String email, String telefono, String rol) throws MiException {

        

        if (username.isEmpty() || username == null) {
            throw new MiException("El nombre de usuario no puede ser nulo o estar vacío");
        }

        if (username != null && !(usuarioRepositorio.findAll().isEmpty())) {
            if (usuarioRepositorio.buscarUsernameRepetido(username)>=1) {
                System.out.println("nombres:"+usuarioRepositorio.buscarUsernameRepetido(username));
                throw new MiException("Nombre de usuario ocupado. Elija otro");
            }
        }

        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacía y debe tener más de 5 dígitos");
        }
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede ser nulo o estar vacío");
        }

        if (email.isEmpty() || email == null) {
            throw new MiException("El email no puede ser nulo o estar vacío");
        }

        if (email.equalsIgnoreCase(usuarioRepositorio.buscarPorEmaill(email))) {
            throw new MiException("El email ingresado ya se encuentra registrado");
        }

        if (telefono.isEmpty() || telefono == null) {
            throw new MiException("El teléfono no puede ser nulo o estar vacío");
        }

        if (rol.isEmpty() || rol == null) {
            throw new MiException("El rol no puede estar vacío");
        }

        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }

    }

    private void validarModificarUsuario(String username, String password, String nombre, String telefono, String rol) throws MiException {

        
        if (username.isEmpty() || username == null) {
            throw new MiException("El nombre de usuario no puede ser nulo o estar vacío");
        }
        
        if (username != null && !(usuarioRepositorio.findAll().isEmpty())) {
            if (usuarioRepositorio.buscarUsernameRepetido(username)!=0) {
                throw new MiException("Nombre de usuario ocupado. Elija otro");
            }
        }

        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacía y debe tener más de 5 dígitos");
        }
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede ser nulo o estar vacío");
        }

        if (telefono.isEmpty() || telefono == null) {
            throw new MiException("El teléfono no puede ser nulo o estar vacío");
        }

        if (rol.isEmpty() || rol == null) {
            throw new MiException("El rol no puede estar vacío");
        }

    }

    public void borrarUsuarioPorId(String id) throws MiException {
        Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(id);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            if (!usuario.getRol().equals(Rol.ADMIN)) {
                usuarioRepositorio.deleteById(id);
            } else {
                throw new MiException("No se puede eliminar al administrador.");
            }
        } else {
            throw new MiException("Usuario no encontrado.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        Boolean estado = usuarioRepositorio.buscarEstado(email);

        if (usuario != null && estado == true) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getUsername(), usuario.getPassword(), permisos);

        } else {
            return null;
        }

    }

}
