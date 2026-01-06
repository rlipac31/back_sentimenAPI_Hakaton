package RichardLipa.sentimentAPI.controller;

import RichardLipa.sentimentAPI.domain.usuario.*;
import RichardLipa.sentimentAPI.infra.DatosToken;
import RichardLipa.sentimentAPI.service.TokenService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    /**
     * Login endpoint
     * POST /auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid DatosLogin datos) {
        try {
            // Crear token de autenticación
            Authentication authToken = new UsernamePasswordAuthenticationToken(
                datos.email(), 
                datos.clave()
            );
            
            // Autenticar
            var usuarioAutenticado = authenticationManager.authenticate(authToken);
            
            // Generar JWT
            var usuario = (Usuario) usuarioAutenticado.getPrincipal();
            var jwtToken = tokenService.generarToken(usuario);
            
            // Crear DTO de usuario para respuesta
            DatosDetalleUsuario usuarioDTO = new DatosDetalleUsuario(usuario);
            
            // Retornar token y datos del usuario
            return ResponseEntity.ok(new DatosAutenticacion(jwtToken, usuarioDTO));
            
        } catch (Exception e) {
            System.err.println("Error en login: " + e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse("Credenciales inválidas"));
        }
    }

    /**
     * Register endpoint
     * POST /auth/register
     */
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> register(@RequestBody @Valid DatosRegistroUsuario datos) {
        try {
            // Verificar si el email ya existe
            Usuario usuarioExistente = usuarioRepository.findByEmailAndStateTrue(datos.email());
            if (usuarioExistente != null) {
                return ResponseEntity.badRequest().body(new ErrorResponse("El email ya está registrado"));
            }
            
            // Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario(datos);
            
            // Encriptar contraseña
            String passwordEncriptada = passwordEncoder.encode(datos.contrasenia());
            nuevoUsuario.setContrasenia(passwordEncriptada);
            nuevoUsuario.setEmail(datos.email());
            
            // Guardar en base de datos
            usuarioRepository.save(nuevoUsuario);
            
            System.out.println("✅ Usuario registrado: " + nuevoUsuario.getEmail());
            
            // Retornar datos del usuario creado
            return ResponseEntity.ok(new DatosDetalleUsuario(nuevoUsuario));
            
        } catch (Exception e) {
            System.err.println("Error en registro: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ErrorResponse("Error al registrar usuario: " + e.getMessage()));
        }
    }
}

// DTOs adicionales
record DatosLogin(
    @jakarta.validation.constraints.NotBlank String email,
    @jakarta.validation.constraints.NotBlank String clave
) {}

record DatosAutenticacion(
    String jwtToken,
    DatosDetalleUsuario usuarioDTO
) {}

record ErrorResponse(
    String message
) {}
