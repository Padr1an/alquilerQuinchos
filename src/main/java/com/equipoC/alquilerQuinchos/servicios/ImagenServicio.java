package com.equipoC.alquilerQuinchos.servicios;

import com.equipoC.alquilerQuinchos.entidades.Comentarios;
import com.equipoC.alquilerQuinchos.entidades.Imagen;
import com.equipoC.alquilerQuinchos.excepciones.MiException;
import com.equipoC.alquilerQuinchos.repositorios.ComentariosRepositorio;
import com.equipoC.alquilerQuinchos.repositorios.ImagenRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Service
public class ImagenServicio {

    @Autowired
    private ImagenRepositorio imagenRepositorio;

    public Imagen guardar(MultipartFile archivo) throws MiException {
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return null;
    }

    /*    public Imagen guardarImgComentario(MultipartFile archivo) throws MiException {


        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return null;
    }*/
    public Imagen actualizar(MultipartFile archivo, String idImagen) throws MiException {
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                if (idImagen != null) {
                    Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);
                    if (respuesta.isPresent()) {
                        imagen = respuesta.get();
                    }
                }
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
                //imagen.setComentarios(); tal vez sea necesario a la hora de hacer los coments.
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    @Transactional
    public void eliminarImagen(String id) throws MiException {
        try {
            Imagen imagen = new Imagen();
            if (id != null) {
                Optional<Imagen> respuesta = imagenRepositorio.findById(id);
                if (respuesta.isPresent()) {
                    imagen = respuesta.get();
                }
            }
            imagenRepositorio.deleteById(id);
        } catch (Exception e) {
            throw new MiException("No se pudo eliminar la imagen");
        }

    }

}
