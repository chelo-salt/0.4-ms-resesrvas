package cl.municipalidad.reservas.dto.request;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DtoReservaRequest {
    private Integer idCancha;
    private Long idUsuario;
    private LocalDate fechaReserva;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}