package RichardLipa.sentimentAPI.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 1. Especifica los dominios exactos (NO uses "*" si envías tokens)
                .allowedOrigins("http://localhost:3000")
                .allowedOrigins("https://fron-sentiment-api.vercel.app/")
                // 2. Permite los métodos necesarios
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 3. ¡IMPORTANTE! Permite la cabecera de Autorización
                .allowedHeaders("Authorization", "Content-Type", "Accept")
                // 4. Permite que el navegador envíe el token
                .allowCredentials(true)
                // 5. Tiempo que el navegador guarda esta configuración (3600 seg = 1 hora)
                .maxAge(3600);
    }
}


