package cl.municipalidad.reservas.repository;

import cl.municipalidad.reservas.model.ReservasModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservasRepository extends JpaRepository<ReservasModel, Long> {

    // 📊 Conteo automático usando las convenciones de nombres de Spring Data JPA
    Integer countByFechaReservaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // 🌟 Busca el nombre de la cancha que más se repite en ese rango de fechas
    @Query("SELECT r.nombreCanchaForaneo FROM ReservasModel r " +
           "WHERE r.fechaReserva BETWEEN :inicio AND :fin " +
           "GROUP BY r.nombreCanchaForaneo " +
           "ORDER BY COUNT(r) DESC")
    List<String> findCanchaEstrella(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin, Pageable pageable);
}