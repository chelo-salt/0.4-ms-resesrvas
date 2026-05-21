package cl.municipalidad.reservas.service;

import cl.municipalidad.reservas.client.CanchasClient;
import cl.municipalidad.reservas.dto.request.DtoReservaRequest;
import cl.municipalidad.reservas.dto.response.DtoCanchaResponse;
import cl.municipalidad.reservas.model.ReservasModel;
import cl.municipalidad.reservas.repository.ReservasRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservasService {

    private final ReservasRepository reservasRepository;
    private final CanchasClient canchasClient;

    // Inyección por constructor (Asegura que las dependencias no sean nulas)
    public ReservasService(ReservasRepository reservasRepository, CanchasClient canchasClient) {
        this.reservasRepository = reservasRepository;
        this.canchasClient = canchasClient;
    }

    public ReservasModel crearReserva(DtoReservaRequest request) {
        // 1. Consultamos de forma remota los datos de la cancha en ms-canchas
        DtoCanchaResponse canchaRemota = canchasClient.consultarCancha(request.getIdCancha());

        // 2. Validación estricta: Si la cancha no existe, bloqueamos la reserva con un Bad Request (400)
        if (canchaRemota == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "No se puede crear la reserva: La cancha especificada no existe o el servicio no está disponible.");
        }

        // 3. Creamos la instancia de nuestro modelo de base de datos
        ReservasModel nuevaReserva = new ReservasModel();
        nuevaReserva.setIdCancha(request.getIdCancha());
        nuevaReserva.setIdUsuario(request.getIdUsuario());
        nuevaReserva.setFechaReserva(request.getFechaReserva());
        nuevaReserva.setHoraInicio(request.getHoraInicio());
        nuevaReserva.setHoraFin(request.getHoraFin());
        
        // Toda reserva nace en estado PENDIENTE
        nuevaReserva.setEstadoReserva("PENDIENTE");
        nuevaReserva.setNombreCanchaForaneo(canchaRemota.getNombre());

        // 4. Guardamos en la base de datos y retornamos el resultado
        return reservasRepository.save(nuevaReserva);
    }

    public ReservasModel confirmarEstadoReserva(Long idReserva, String nuevoEstado) {
        // Usamos ResponseStatusException para devolver automáticamente un error HTTP 404 si no se encuentra
        ReservasModel reserva = reservasRepository.findById(idReserva)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada con el ID: " + idReserva));
        
        // Modificamos el estado
        reserva.setEstadoReserva(nuevoEstado.toUpperCase());
        
        // Guardamos los cambios
        return reservasRepository.save(reserva);
    }

    /**
     * 📊 Lógica analítica: Cuenta la cantidad total de reservas registradas en el rango.
     */
    public Integer contarReservasPorRango(LocalDate inicio, LocalDate fin) {
        Integer conteo = reservasRepository.countByFechaReservaBetween(inicio, fin);
        return conteo != null ? conteo : 0;
    }

    /**
     * 🌟 Lógica analítica: Retorna el nombre de la cancha más arrendada del periodo.
     */
    public String obtenerCanchaEstrella(LocalDate inicio, LocalDate fin) {
        // Le pasamos PageRequest.of(0, 1) para que funcione como un LIMIT 1 de SQL y devuelva solo la principal
        List<String> resultados = reservasRepository.findCanchaEstrella(inicio, fin, PageRequest.of(0, 1));
        
        if (resultados != null && !resultados.isEmpty() && resultados.get(0) != null) {
            return resultados.get(0);
        }
        return "Sin datos";
    }
}