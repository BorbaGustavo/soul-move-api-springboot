package br.com.soulMove.soul_move_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Libera todas as rotas da API
                .allowedOrigins("*") // Permite requisições de qualquer origem (em produção, você pode restringir para o domínio do seu front)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH") // Métodos HTTP permitidos
                .allowedHeaders("*"); // Todos os headers permitidos
    }
}