package RichardLipa.sentimentAPI.domain.comentario;

import RichardLipa.sentimentAPI.domain.usuario.Usuario;
import lombok.extern.java.Log;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record DatosListaComentarios(
        Long id,
        String comentario,
        Tipo tipo,
        String usuario,
        String fecharegistro
) {
    public DatosListaComentarios(Comentario comentario) {
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
