package com.plagiarism.AntiPlagiarismBack.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class Config implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Указывает, что CORS доступен для всех путей
                .allowedOrigins("http://localhost:5173") // Указывает, откуда можно делать запросы
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Указывает, какие HTTP методы разрешены
                .allowCredentials(true) // Указывает, разрешены ли куки
                .maxAge(3600); // Получение кэша на 1 час
    }
}