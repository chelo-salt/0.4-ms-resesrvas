package cl.municipalidad.reservas.service;

import cl.municipalidad.reservas.client.CanchasClient;
import cl.municipalidad.reservas.dto.request.DtoReservaRequest;
import cl.municipalidad.reservas.dto.response.DtoCanchaResponse;
import cl.municipalidad.reservas.model.ReservasModel;
import cl.municipalidad.reservas.repository.ReservasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservasService {

    @Autowired
    private ReservasRepository reservasRepository;

    @Autowired
    private CanchasClient canchasClient;

    public ReservasModel crearReserva(DtoReservaRequest request) {
        // 1. Consultamos de forma remota los datos de la cancha en ms-canchas
        DtoCanchaResponse canchaRemota = canchasClient.consultarCancha(request.getIdCancha());

        // 2. Creamos la instancia de nuestro modelo de base de datos
        ReservasModel nuevaReserva = new ReservasModel();
        nuevaReserva.setIdCancha(request.getIdCancha());
        nuevaReserva.setIdUsuario(request.getIdUsuario());
        nuevaReserva.setFechaReserva(request.getFechaReserva());
        nuevaReserva.setHoraInicio(request.getHoraInicio());
        nuevaReserva.setHoraFin(request.getHoraFin());
        
        // Toda reserva nace en estado PENDIENTE hasta que ms-pagos la valide
        nuevaReserva.setEstadoReserva("PENDIENTE");

        // 3. Si la cancha existía y el cliente web nos trajo datos, guardamos el nombre como auditoría
        if (canchaRemota != null) {
            nuevaReserva.setNombreCanchaForaneo(canchaRemota.getNombre());
        } else {
            nuevaReserva.setNombreCanchaForaneo("Cancha Desconocida (Módulo Canchas Inaccesible)");
        }

        // 4. Guardamos en MySQL y retornamos el resultado
        return reservasRepository.save(nuevaReserva);
    }

    public ReservasModel confirmarEstadoReserva(Long idReserva, String nuevoEstado) {
    // 1. Buscamos la reserva en la base de datos. Si no existe, lanzamos un error básico
    ReservasModel reserva = reservasRepository.findById(idReserva)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada con el ID: " + idReserva));
    
    // 2. Modificamos el estado (Ej: CONFIRMADA o CANCELADA)
    reserva.setEstadoReserva(nuevoEstado.toUpperCase());
    
    // 3. Guardamos los cambios actualizados en MySQL
    return reservasRepository.save(reserva);
    }
}