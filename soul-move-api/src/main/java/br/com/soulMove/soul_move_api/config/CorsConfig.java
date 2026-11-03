package br.com.soulMove.soul_move_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {
    // Lê a propriedade do application.properties (com um fallback para localhost caso não ache)
    @Value("${cors.allowed-origins:http://localhost:5173,https://challenge-frontend-soul-move.vercel.app}")
    private String allowedOrigins;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));

        // Definindo os métodos HTTP que podem ser acessados (Corrigido "PATCH")
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // Informando as propriedades do cabeçalho
        config.setAllowedHeaders(List.of("Content-Type", "Authorization"));

        // Configuração de credenciais (opcional, caso utilizes cookies ou sessão)
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
