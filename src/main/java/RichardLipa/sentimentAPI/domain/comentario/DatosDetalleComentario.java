package RichardLipa.sentimentAPI.domain.comentario;

import lombok.extern.java.Log;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record DatosDetalleComentario(
        Long id,
        String comentario,
        Tipo previson,
        String fechaRegistro
) {

    public DatosDetalleComentario(Comentario comentario) {
        this(
                comentario.getId(),
                comentario.getComentario(),
                comentario.getPrevision(),
                comentario.getFechaRegistro().format(
                        DateTimeFormatter.ofPattern( "d 'de' MMMM 'del' yyyy 'a las' h:mm a", new Locale("es", "ES"))
                )
        );

    }

}
