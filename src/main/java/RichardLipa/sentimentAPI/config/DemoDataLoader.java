package RichardLipa.sentimentAPI.config;

import RichardLipa.sentimentAPI.domain.usuario.IUsuarioRepository;
import RichardLipa.sentimentAPI.domain.usuario.Usuario;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DemoDataLoader {

    @Bean
    CommandLineRunner initDatabase(IUsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verificar si ya existe el usuario demo
            Usuario usuarioDemo = usuarioRepository.findByEmailAndStateTrue("demo@sentimentai.com");
            
            if (usuarioDemo == null) {
                // Crear usuario demo usando constructor completo
                RichardLipa.sentimentAPI.domain.usuario.DatosRegistroUsuario datosDemo = 
                    new RichardLipa.sentimentAPI.domain.usuario.DatosRegistroUsuario(
                        "Demo User",
                        "demo@sentimentai.com",
                        passwordEncoder.encode("demo123"),
                        RichardLipa.sentimentAPI.domain.usuario.Role.USER,
                        java.time.LocalDateTime.now(),
                        true
                    );
                Usuario demo = new Usuario(datosDemo);
                
                usuarioRepository.save(demo);
                
                System.out.println("✅ Usuario demo creado exitosamente:");
                System.out.println("   📧 Email: demo@sentimentai.com");
                System.out.println("   🔑 Password: demo123");
            } else {
                System.out.println("ℹ️  Usuario demo ya existe en la base de datos");
            }
        };
    }
}
