package cl.municipalidad.reservas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    @Column(nullable = false)
    private Integer idCancha; // Para enlazar con ms-canchas

    @Column(nullable = false)
    private Long idUsuario; // Para enlazar con ms-auth (el vecino que reserva)

    @Column(nullable = false)
    private LocalDate fechaReserva; // El día que se usará la cancha

    @Column(nullable = false)
    private LocalTime horaInicio; // Ejemplo: 19:00

    @Column(nullable = false)
    private LocalTime horaFin; // Ejemplo: 20:00

    @Column(nullable = false)
    private String estadoReserva; // PENDIENTE, CONFIRMADA, CANCELADA

    private String nombreCanchaForaneo; // Guardaremos el nombre que WebClient traerá de ms-canchas
}