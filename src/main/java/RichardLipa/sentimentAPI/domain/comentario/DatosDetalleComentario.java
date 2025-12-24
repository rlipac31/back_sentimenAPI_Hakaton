package RichardLipa.sentimentAPI.domain.comentario;

import lombok.extern.java.Log;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record DatosDetalleComentario(
        Long id,
        String comentario,
        Tipo tipo,
        String usuario,
        String fechaRegistro
) {

    public DatosDetalleComentario(Comentario comentario) {
        this(
                comentario.getId(),
                comentario.getComentario(),
                comentario.getTipo(),
                comentario.getUsuario().getEmail(),
                comentario.getFechaRegistro().format(
                        DateTimeFormatter.ofPattern( "d 'de' MMMM 'del' yyyy 'a las' h:mm a", new Locale("es", "ES"))
                )
        );

    }

}
