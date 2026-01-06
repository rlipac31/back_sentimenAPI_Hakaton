package RichardLipa.sentimentAPI.domain.comentario;

import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IComentarioRepository extends JpaRepository<Comentario, Long> {
    Page<Comentario> findAllByStateTrue(Pageable paginacion);
    long countByTipoAndStateTrue(Tipo tipo);
    
    // Contar comentarios por tipo en un rango de fechas
    @Query("SELECT COUNT(c) FROM Comentario c WHERE c.tipo = :tipo AND c.state = true AND c.fechaRegistro >= :fechaInicio AND c.fechaRegistro < :fechaFin")
    long countByTipoAndStateTrueAndFechaRegistroBetween(
        @Param("tipo") Tipo tipo, 
        @Param("fechaInicio") LocalDateTime fechaInicio, 
        @Param("fechaFin") LocalDateTime fechaFin
    );
    
    // Obtener comentarios de los últimos N días
    List<Comentario> findByStateTrueAndFechaRegistroAfterOrderByFechaRegistroDesc(LocalDateTime fecha);
}
