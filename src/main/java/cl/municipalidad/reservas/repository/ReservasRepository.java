package cl.municipalidad.reservas.repository;

import cl.municipalidad.reservas.model.ReservasModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservasRepository extends JpaRepository<ReservasModel, Long> {
    // Listo, JPA hereda automáticamente todos los métodos de guardar, buscar y borrar.
}