package RichardLipa.sentimentAPI.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
    @Override
    public  void addCorsMappings (CorsRegistry registry){
        registry.addMapping("/**")
                // Permite cualquier origen (cualquier IP, cualquier dominio, incluyendo ngrok)
                .allowedOrigins("*")
                //   // Orígenes permitidos (por ejemplo, tu frontend de React/Angular)
                //.allowedOrigins("http://localhost:3000","https://tudominiofrontend.com")
                // // Métodos HTTP permitidos
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");



    }
}


