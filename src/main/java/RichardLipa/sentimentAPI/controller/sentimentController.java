package RichardLipa.sentimentAPI.controller;


import RichardLipa.sentimentAPI.domain.comentario.*;
import RichardLipa.sentimentAPI.domain.usuario.IUsuarioRepository;
import RichardLipa.sentimentAPI.domain.usuario.Usuario;
import RichardLipa.sentimentAPI.service.SentimientoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/sentiment")
public class sentimentController {


    @Autowired
    private SentimientoService service;

    @Autowired
    private IComentarioRepository comentarioRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<List<DatosRespuestaSentimiento>>  analizar(
            @RequestBody List<DatosTextoJson> datos,
            Authentication authentication) {
        
        // 1. Obtener usuario autenticado o usar usuario demo
        Usuario usuario = obtenerUsuario(authentication);
        
        // 2. Procesar los textos con el modelo ML
        List<DatosRespuestaSentimiento> resultados = service.procesarLista(datos);
        
        // 3. Guardar cada resultado en la base de datos
        for (int i = 0; i < resultados.size(); i++) {
            DatosRespuestaSentimiento resultado = resultados.get(i);
            String textoOriginal = datos.get(i).texto();
            
            // Convertir la predicción a Tipo enum
            Tipo tipo = convertirATipo(resultado.prevision());
            
            // Crear y guardar el comentario
            Comentario comentario = new Comentario();
            comentario.setComentario(textoOriginal);
            comentario.setTipo(tipo);
            comentario.setUsuario(usuario);
            comentario.setFechaRegistro(java.time.LocalDateTime.now());
            comentario.setState(true);
            
            comentarioRepository.save(comentario);
        }
        
        System.out.println("✅ Análisis guardados en BD: " + resultados.size() + " comentarios");
        
        // 4. Devolver los resultados al frontend
        return ResponseEntity.ok(resultados);
    }

    private Usuario obtenerUsuario(Authentication authentication) {
        // Intentar obtener usuario autenticado
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Usuario usuario = usuarioRepository.findByEmailAndStateTrue(email);
            if (usuario != null) {
                return usuario;
            }
        }
        
        // Si no hay usuario autenticado, usar usuario demo
        Usuario usuarioDemo = usuarioRepository.findByEmailAndStateTrue("demo@sentimentai.com");
        
        // Si no existe el usuario demo, crearlo
        if (usuarioDemo == null) {
            System.out.println("⚠️ Usuario demo no encontrado. Buscando cualquier usuario...");
            List<Usuario> usuarios = usuarioRepository.findAll();
            if (!usuarios.isEmpty()) {
                return usuarios.get(0);
            } else {
                throw new RuntimeException("No hay usuarios en la base de datos");
            }
        }
        
        return usuarioDemo;
    }

    private Tipo convertirATipo(String prevision) {
        if (prevision == null) {
            return Tipo.NEUTRO;
        }
        
        String previsionLower = prevision.toLowerCase();
        if (previsionLower.contains("positivo") || previsionLower.equals("positive")) {
            return Tipo.POSITIVO;
        } else if (previsionLower.contains("negativo") || previsionLower.equals("negative")) {
            return Tipo.NEGATIVO;
        } else {
            return Tipo.NEUTRO;
        }
    }

}


