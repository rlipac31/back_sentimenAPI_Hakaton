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

   // private final String PYTHON_API_URL = "https://tu-url-de-colab.ngrok-free.app/predict";
   private final String PYTHON_API_URL = "https://ee977718c8cc.ngrok-free.app/sentiment";

    public DatosRespuestaSentimiento analizarSentimiento(String texto) {
        RestTemplate restTemplate = new RestTemplate();

        // Creamos el cuerpo de la petición para Python
        Map<String, String> request = new HashMap<>();
        request.put("text", texto);

        // Llamada al microservicio de Data Science
        return restTemplate.postForObject(PYTHON_API_URL, request, DatosRespuestaSentimiento.class);
    }

    // Ejemplo de cómo llamarías al Colab desde tu Service de Java
    public List<DatosRespuestaSentimiento> procesarComentarios(List<DatosTextoJson> lista) {
        String urlColab = "https://ee977718c8cc.ngrok-free.app/sentiment";
        RestTemplate rest = new RestTemplate();

        return lista.stream().map(comentario -> {
            // Creamos el objeto que espera Python: { "text": "..." }
            Map<String, String> body = Map.of("text", comentario.texto());

            // Enviamos y recibimos la predicción
            return rest.postForObject(urlColab, body, DatosRespuestaSentimiento.class);
        }).toList();
    }

    ///  rpocesar lista


    // IMPORTANTE: Aquí pegarás la URL que te dé Google Colab cada vez que lo inicies
    private final String COLAB_URL = "https://9382c7ff6643.ngrok-free.app/sentiment";

    public List<DatosRespuestaSentimiento> procesarLista(List<DatosTextoJson> datos) {
        RestTemplate restTemplate = new RestTemplate();

        // Transformamos cada comentario en una llamada al Colab
        return datos.stream().map(comentario -> {
            // Creamos el cuerpo que espera Python: {"text": "el texto del comentario"}
            Map<String, String> request = Map.of("text", comentario.texto());

            // Hacemos el POST al Colab y convertimos la respuesta en nuestro Record
            return restTemplate.postForObject(COLAB_URL, request, DatosRespuestaSentimiento.class);
        }).toList();
    }

}

