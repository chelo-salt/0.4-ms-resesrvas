package cl.municipalidad.reservas.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
// CORREGIDO: Ahora apunta al nuevo subpaquete response
import cl.municipalidad.reservas.dto.response.DtoCanchaResponse;

@Component
public class CanchasClient {

    @Autowired
    @Qualifier("webClientCanchas")
    private WebClient webClient;

    // CORREGIDO: Retorna el DTO desde la ubicación correcta
    public DtoCanchaResponse consultarCancha(Integer idCancha) {
    try {
        return webClient.get()
                .uri("/api/v1/cancha/" + idCancha) // ¡CORREGIDO: Sin la "s"!
                .retrieve()
                .bodyToMono(DtoCanchaResponse.class)
                .block(); 
    } catch (Exception e) {
        System.out.println("🚨 ERROR CRÍTICO WEBCLIENT: " + e.getMessage());
        e.printStackTrace(); 
        return null;
    }
    }
}