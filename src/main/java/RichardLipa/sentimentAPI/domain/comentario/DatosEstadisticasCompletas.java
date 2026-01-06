package RichardLipa.sentimentAPI.domain.comentario;

import java.util.List;

public record DatosEstadisticasCompletas(
    // Totales generales
    long totalComentarios,
    long positivos,
    long negativos,
    long neutros,
    double porcentajePositivos,
    double porcentajeNegativos,
    double porcentajeNeutros,
    
    // Estadísticas por día (últimos 7 días)
    List<DatosEstadisticasPorDia> ultimosDias,
    
    // Análisis general
    String sentimientoGeneral, // "Mayormente Positivo", "Mayormente Negativo", "Equilibrado"
    double promedioPositividad // Porcentaje promedio de positividad
) {
}
