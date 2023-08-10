package com.equipoC.alquilerQuinchos.servicios;

import com.equipoC.alquilerQuinchos.entidades.Comentarios;
import com.equipoC.alquilerQuinchos.entidades.Imagen;
import com.equipoC.alquilerQuinchos.entidades.Inmueble;
import com.equipoC.alquilerQuinchos.entidades.Usuario;
import com.equipoC.alquilerQuinchos.excepciones.MiException;
import com.equipoC.alquilerQuinchos.repositorios.ComentariosRepositorio;
import com.equipoC.alquilerQuinchos.repositorios.ImagenRepositorio;
import com.equipoC.alquilerQuinchos.repositorios.InmuebleRepositorio;
import com.equipoC.alquilerQuinchos.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ComentariosServicio {

    @Autowired
    private ComentariosRepositorio comentarioRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ImagenServicio imagenServicio;
    @Autowired
    private ImagenRepositorio imagenRepositorio;
    @Autowired
    private InmuebleRepositorio inmuebleRepositorio;

    @Transactional
    public void crearComentario(String idCliente, Long idInmueble, String comentario, int calificacion, Date fecha, List<MultipartFile> fotos) throws MiException {

        validarComentario(comentario, calificacion, fotos);


        Comentarios opinion = new Comentarios();
        Usuario usuario = usuarioRepositorio.buscarPorId(idCliente);


        opinion.setUserCliente(usuario);
        opinion.setComentario(comentario);
        opinion.setCalificacion(calificacion);
        opinion.setFechaPublicacion(fecha);


        List<Imagen> imgComent = new ArrayList<>();

        for (MultipartFile archivoImagen : fotos){
            Imagen imagen = imagenServicio.guardar(archivoImagen);
            imgComent.add(imagen);
        }
        opinion.setFotos(imgComent);


        Inmueble inmueble = inmuebleRepositorio.buscarPorId(idInmueble);
        opinion.setInmueble(inmueble);
        comentarioRepositorio.save(opinion);

        for (Imagen img : imgComent
             ) {
            img.setComentarios(opinion);
        }

    }

    @Transactional

    public List<Comentarios> listarComentarios() {

        List<Comentarios> comentario = new ArrayList();

        comentario = comentarioRepositorio.findAll();

        return comentario;

    }

    public List<Comentarios> listarComentariosPorCalificacionDesc() {

        List<Comentarios> comentario = new ArrayList();

        comentario = comentarioRepositorio.findAllOrderByCalificacionDesc();

        return comentario;

    }

    @Transactional
    public void modificarComentario(String id, String idImg, String comentario, int calificacion, List<MultipartFile> fotos) throws MiException {

        validarComentario(comentario, calificacion, fotos);

        Optional<Comentarios> coment = comentarioRepositorio.findById(id);

        Comentarios actualizarComentario = new Comentarios();

        if (coment.isPresent()) {

            actualizarComentario = coment.get();
            ;
            actualizarComentario.setComentario(comentario);
            actualizarComentario.setCalificacion(calificacion);


            ArrayList <Imagen> listaImagen = new ArrayList<>();
            Imagen imagen = new Imagen();


            /*if (actualizarComentario.getFotos() != null){


            if (actualizarComentario.getFotos() != null){

                imagen = imagenServicio.actualizar(fotos, idImg);
            }*/
            listaImagen.add(imagen);
            actualizarComentario.setFotos(listaImagen);

            comentarioRepositorio.save(actualizarComentario);

        }


    }

    @Transactional
    public void eliminarComentario(String id) {


        Optional<Comentarios> comentarios = comentarioRepositorio.findById(id);

        if (comentarios.isPresent()) {

            Comentarios comentario = comentarios.get();

            comentarioRepositorio.delete(comentario);

        }

    }

    @Transactional
    public Comentarios getOne(String id) {

        return comentarioRepositorio.getOne(id);

    }


    private void validarComentario(String comentario, int calificacion, List<MultipartFile> fotos) throws MiException {

        if (comentario == null || comentario.isEmpty()) {


            throw new MiException("el comentario no puede ser nulo o estar vacio");

        }

        if (calificacion == 0) {

            throw new MiException("la calificacion no puede ser cero");

        }

        if (fotos == null || fotos.isEmpty()) {

            throw new MiException("las fotos no pueden ser nulas o vacias");

        }


    }
}
