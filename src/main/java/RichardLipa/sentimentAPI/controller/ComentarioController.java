package RichardLipa.sentimentAPI.controller;

import RichardLipa.sentimentAPI.domain.comentario.*;
import RichardLipa.sentimentAPI.domain.usuario.Usuario;
import RichardLipa.sentimentAPI.domain.usuario.IUsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private IComentarioRepository comentarioRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    /**
     * Crear un nuevo comentario con su análisis de sentimiento
     * POST /comentarios
     */
    @PostMapping
    @Transactional
    public ResponseEntity<DatosDetalleComentario> crearComentario(
            @RequestBody @Valid DatosRegistroComentario datos,
            Authentication authentication) {
        
        // Obtener el usuario autenticado (si existe)
        Usuario usuario = null;
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            usuario = usuarioRepository.findByEmailAndStateTrue(email);
        }
        
        // Si no hay usuario autenticado, usar usuario por defecto (para demo)
        if (usuario == null) {
            // Buscar o crear usuario "demo"
            usuario = usuarioRepository.findByEmailAndStateTrue("demo@sentimentai.com");
            if (usuario == null) {
                // Si no existe, retornar error
                return ResponseEntity.badRequest().build();
            }
        }

        // Crear el comentario
        Comentario comentario = new Comentario(datos, usuario);
        comentarioRepository.save(comentario);

        return ResponseEntity.ok(new DatosDetalleComentario(comentario));
    }

    /**
     * Listar todos los comentarios con paginación
     * GET /comentarios?page=0&size=10&sort=fechaRegistro,desc
     */
    @GetMapping
    public ResponseEntity<Page<DatosListaComentarios>> listarComentarios(
            @PageableDefault(size = 10, sort = {"fechaRegistro"}) Pageable paginacion) {
        
        var page = comentarioRepository.findAllByStateTrue(paginacion)
                .map(DatosListaComentarios::new);
        
        return ResponseEntity.ok(page);
    }

    /**
     * Obtener detalle de un comentario específico
     * GET /comentarios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleComentario> obtenerComentario(@PathVariable Long id) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        
        return ResponseEntity.ok(new DatosDetalleComentario(comentario));
    }

    /**
     * Eliminar un comentario (soft delete)
     * DELETE /comentarios/{id}
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarComentario(@PathVariable Long id) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        
        comentario.desactivar();
        
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtener estadísticas de sentimientos
     * GET /comentarios/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<DatosEstadisticas> obtenerEstadisticas() {
        long totalComentarios = comentarioRepository.count();
        long positivos = comentarioRepository.countByTipoAndStateTrue(Tipo.POSITIVO);
        long negativos = comentarioRepository.countByTipoAndStateTrue(Tipo.NEGATIVO);
        long neutros = comentarioRepository.countByTipoAndStateTrue(Tipo.NEUTRO);

        double porcentajePositivos = totalComentarios > 0 ? (positivos * 100.0 / totalComentarios) : 0;
        double porcentajeNegativos = totalComentarios > 0 ? (negativos * 100.0 / totalComentarios) : 0;
        double porcentajeNeutros = totalComentarios > 0 ? (neutros * 100.0 / totalComentarios) : 0;

        return ResponseEntity.ok(new DatosEstadisticas(
                totalComentarios,
                positivos,
                negativos,
                neutros,
                porcentajePositivos,
                porcentajeNegativos,
                porcentajeNeutros
        ));
    }
    
    /**
     * Obtener estadísticas completas con análisis temporal
     * GET /comentarios/stats-completas
     */
    @GetMapping("/stats-completas")
    public ResponseEntity<DatosEstadisticasCompletas> obtenerEstadisticasCompletas() {
        // Totales generales
        long totalComentarios = comentarioRepository.count();
        long positivos = comentarioRepository.countByTipoAndStateTrue(Tipo.POSITIVO);
        long negativos = comentarioRepository.countByTipoAndStateTrue(Tipo.NEGATIVO);
        long neutros = comentarioRepository.countByTipoAndStateTrue(Tipo.NEUTRO);

        double porcentajePositivos = totalComentarios > 0 ? (positivos * 100.0 / totalComentarios) : 0;
        double porcentajeNegativos = totalComentarios > 0 ? (negativos * 100.0 / totalComentarios) : 0;
        double porcentajeNeutros = totalComentarios > 0 ? (neutros * 100.0 / totalComentarios) : 0;

        // Estadísticas por día (últimos 7 días)
        List<DatosEstadisticasPorDia> estadisticasPorDia = new java.util.ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();
        
        for (int i = 6; i >= 0; i--) {
            LocalDateTime inicioDia = ahora.minusDays(i).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime finDia = inicioDia.plusDays(1);
            
            long positivosDia = comentarioRepository.countByTipoAndStateTrueAndFechaRegistroBetween(
                Tipo.POSITIVO, inicioDia, finDia);
            long negativosDia = comentarioRepository.countByTipoAndStateTrueAndFechaRegistroBetween(
                Tipo.NEGATIVO, inicioDia, finDia);
            long neutrosDia = comentarioRepository.countByTipoAndStateTrueAndFechaRegistroBetween(
                Tipo.NEUTRO, inicioDia, finDia);
            long totalDia = positivosDia + negativosDia + neutrosDia;
            
            estadisticasPorDia.add(new DatosEstadisticasPorDia(
                inicioDia.toLocalDate(),
                positivosDia,
                negativosDia,
                neutrosDia,
                totalDia
            ));
        }
        
        // Determinar sentimiento general
        String sentimientoGeneral;
        if (porcentajePositivos > 50) {
            sentimientoGeneral = "Mayormente Positivo";
        } else if (porcentajeNegativos > 50) {
            sentimientoGeneral = "Mayormente Negativo";
        } else {
            sentimientoGeneral = "Equilibrado";
        }
        
        return ResponseEntity.ok(new DatosEstadisticasCompletas(
            totalComentarios,
            positivos,
            negativos,
            neutros,
            porcentajePositivos,
            porcentajeNegativos,
            porcentajeNeutros,
            estadisticasPorDia,
            sentimientoGeneral,
            porcentajePositivos
        ));
    }
}
