package cl.municipalidad.reservas.controller;

import cl.municipalidad.reservas.dto.request.DtoReservaRequest;
import cl.municipalidad.reservas.model.ReservasModel;
import cl.municipalidad.reservas.service.ReservasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservasController {

    @Autowired
    private ReservasService reservasService;

    @PostMapping
    public ResponseEntity<ReservasModel> generarNuevaReserva(@RequestBody DtoReservaRequest request) {
        ReservasModel reservaGuardada = reservasService.crearReserva(request);
        return ResponseEntity.ok(reservaGuardada);
    }
    // Actualizar el estado de una reserva (Invocado principalmente por ms-pagos)
    @PutMapping("/{id}/estado")
    public ResponseEntity<ReservasModel> actualizarEstado(
            @PathVariable("id") Long id,
            @RequestParam("nuevoEstado") String nuevoEstado) {
        
        ReservasModel reservaActualizada = reservasService.confirmarEstadoReserva(id, nuevoEstado);
        return ResponseEntity.ok(reservaActualizada);
    }
}