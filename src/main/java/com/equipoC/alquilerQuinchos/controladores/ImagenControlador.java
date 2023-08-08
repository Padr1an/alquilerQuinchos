package com.equipoC.alquilerQuinchos.controladores;

import com.equipoC.alquilerQuinchos.entidades.Imagen;
import com.equipoC.alquilerQuinchos.entidades.Inmueble;
import com.equipoC.alquilerQuinchos.entidades.Usuario;
import com.equipoC.alquilerQuinchos.excepciones.MiException;
import com.equipoC.alquilerQuinchos.repositorios.ImagenRepositorio;
import com.equipoC.alquilerQuinchos.servicios.ImagenServicio;
import com.equipoC.alquilerQuinchos.servicios.InmuebleServicio;
import com.equipoC.alquilerQuinchos.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {

    @Autowired
    UsuarioServicio usuarioservicio;

    @Autowired
    InmuebleServicio inmuebleservicio;
    @Autowired
    ImagenServicio imagenServicio;
    @Autowired
    ImagenRepositorio imagenRepositorio;

    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id) {

        Usuario usuario = usuarioservicio.getOne(id);
        byte[] imagen = usuario.getImagen().getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

    @GetMapping("/inmueble/{id}")
    public ResponseEntity<byte[]> imagenInmueble(@PathVariable Long id) {

        Inmueble inmueble = inmuebleservicio.getOne(id);
        byte[] imagen = inmueble.getImagenInmueble().get(0).getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }
    @GetMapping("/lista/{id}")
    public ResponseEntity<byte[]> listaImagen(@PathVariable String id) {
        Imagen imagine = imagenRepositorio.getOne(id);
        byte[] imagen = imagine.getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);

    }
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") String id) throws MiException {

        imagenServicio.eliminarImagen(id);

        return "redirect:../../inmueble/mis_inmuebles";

    }
}
