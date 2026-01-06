package RichardLipa.sentimentAPI.domain.comentario;

public record DatosEstadisticas(
        Long totalComentarios,
        Long positivos,
        Long negativos,
        Long neutros,
        Double porcentajePositivos,
        Double porcentajeNegativos,
        Double porcentajeNeutros
) {
}
