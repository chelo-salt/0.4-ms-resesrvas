package cl.municipalidad.reservas.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import cl.municipalidad.reservas.dto.response.DtoCanchaResponse;

@Component
public class CanchasClient {

    private static final Logger logger = LoggerFactory.getLogger(CanchasClient.class);
    private final WebClient webClient;

    public CanchasClient(@Qualifier("webClientCanchas") WebClient webClient) {
        this.webClient = webClient;
    }

    public DtoCanchaResponse consultarCancha(Integer idCancha) {
        try {
            return webClient.get()
                    // 🗺️ Ruta corregida para calzar con el @RequestMapping del CanchaController
                    .uri("/api/v1/canchas/cancha/" + idCancha) 
                    .retrieve()
                    .bodyToMono(DtoCanchaResponse.class)
                    .block(); 
        } catch (WebClientResponseException.NotFound e) {
            // Sabemos que la cancha no existe, lo registramos como advertencia
            logger.warn("Cancha con ID {} no encontrada (404) en ms-canchas.", idCancha);
            return null;
        } catch (Exception e) {
            // Error real de red, timeout, o 500 del servidor
            logger.error("🚨 ERROR CRÍTICO WEBCLIENT al consultar cancha {}: {}", idCancha, e.getMessage(), e);
            return null;
        }
    }
}