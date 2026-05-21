package cl.municipalidad.reservas.controller;

import cl.municipalidad.reservas.dto.request.DtoReservaRequest;
import cl.municipalidad.reservas.model.ReservasModel;
import cl.municipalidad.reservas.service.ReservasService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservasController {

    private final ReservasService reservasService;

    // Inyección por constructor (Mejor práctica para inmutabilidad y testing)
    public ReservasController(ReservasService reservasService) {
        this.reservasService = reservasService;
    }

    @PostMapping
    public ResponseEntity<ReservasModel> generarNuevaReserva(@Valid @RequestBody DtoReservaRequest request) {
        ReservasModel reservaGuardada = reservasService.crearReserva(request);
        // Retornamos HTTP 201 Created para la creación exitosa de un recurso
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaGuardada);
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