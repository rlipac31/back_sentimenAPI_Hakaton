package RichardLipa.sentimentAPI.domain.comentario;

import RichardLipa.sentimentAPI.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.time.LocalDateTime;




@Table(name="comentarios")
@Entity(name="Comentario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( of = "id")
public class Comentario {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
   private Long id;
   private String comentario;
   @Enumerated(EnumType.STRING)
   private Tipo tipo;
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_usuario")
   private Usuario usuario;
   private LocalDateTime fechaRegistro; // LocalDateTime.now();
   private Boolean state;

    public Comentario(@Valid DatosRegistroComentario datos, Usuario usuario) {
        this.id = null;
        this.comentario = datos.comentario();
        this.tipo = datos.tipo();
        this.usuario = usuario;
        this.fechaRegistro = LocalDateTime.now();
        this.state = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}

