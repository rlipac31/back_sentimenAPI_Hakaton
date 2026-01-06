package RichardLipa.sentimentAPI.service;

import RichardLipa.sentimentAPI.domain.comentario.DatosRespuestaSentimiento;
import RichardLipa.sentimentAPI.domain.comentario.DatosTextoJson;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SentimientoService {

   // URL PERMANENTE del API desplegada en Render
   private final String PYTHON_API_URL = "https://api-datascience-moyu.onrender.com/sentiment";

    public DatosRespuestaSentimiento analizarSentimiento(String texto) {
        RestTemplate restTemplate = new RestTemplate();

        // Creamos el cuerpo de la petición para Python
        Map<String, String> request = new HashMap<>();
        request.put("text", texto);

        // Llamada al microservicio de Data Science en Render
        return restTemplate.postForObject(PYTHON_API_URL, request, DatosRespuestaSentimiento.class);
    }

    // Procesar lista completa de comentarios
    public List<DatosRespuestaSentimiento> procesarLista(List<DatosTextoJson> datos) {
        RestTemplate restTemplate = new RestTemplate();

        // Transformamos cada comentario en una llamada a la API de Render
        return datos.stream().map(comentario -> {
            // Creamos el cuerpo que espera Python: {"text": "el texto del comentario"}
            Map<String, String> request = Map.of("text", comentario.texto());

            // Hacemos el POST a Render y convertimos la respuesta en nuestro Record
            return restTemplate.postForObject(PYTHON_API_URL, request, DatosRespuestaSentimiento.class);
        }).toList();
    }

}

