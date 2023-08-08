
package com.equipoC.alquilerQuinchos.entidades;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@ToString(onlyExplicitlyIncluded = true)
@Data
public class Imagen {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String mime; 
    private String nombre;
    
   
    @Lob @Basic(fetch= FetchType.LAZY)
    private byte[] contenido;
    
    @ManyToOne
    private Inmueble inmueble;

    @ManyToOne
    private Comentarios comentarios;
    
}
