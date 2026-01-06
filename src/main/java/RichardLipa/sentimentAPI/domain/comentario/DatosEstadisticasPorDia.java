package RichardLipa.sentimentAPI.domain.comentario;

import java.time.LocalDate;

public record DatosEstadisticasPorDia(
    LocalDate fecha,
    long positivos,
    long negativos,
    long neutros,
    long total
) {
}
