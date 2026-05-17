package cl.municipalidad.reservas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean(name = "webClientCanchas")
    public WebClient webClientCanchas() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081") // Puerto del ms-canchas
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}