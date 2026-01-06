package RichardLipa.sentimentAPI.service;

import RichardLipa.sentimentAPI.domain.comentario.DatosRespuestaSentimiento;
import RichardLipa.sentimentAPI.domain.comentario.DatosTextoJson;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//para leer csv

///////
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Writer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SentimientoService {

    /*
    *
    * ruta de url ser servidor de api python
    * <iframe
	src="https://rlipac-python-api.hf.space"
	frameborder="0"
	width="850"
	height="450"
></iframe>

    *
    * */

    //private final String COLAB_URL = "http://0.0.0.0:8000/predict";//servidor local
   private final String COLAB_URL = "https://rlipac-python-api.hf.space/predict";

    public List<DatosRespuestaSentimiento> procesarLista(List<DatosTextoJson> datos) {
        System.out.println("texto antes de procesar :::  " + datos);
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("datos:::" + datos);

        // Transformamos cada comentario en una llamada al Colab
        return datos.stream().map(comentario -> {
            try{
                // Creamos el cuerpo que espera Python: {"text": "el texto del comentario"}
                Map<String, String> request = Map.of("texto", comentario.texto());

                // Hacemos el POST al Colab y convertimos la respuesta en nuestro Record
                return restTemplate.postForObject(COLAB_URL, request, DatosRespuestaSentimiento.class);
            } catch (Exception e) {
                System.err.println("ERROR AL CONECTAR CON COLAB: " + e.getMessage());
                return new DatosRespuestaSentimiento("Error de conexión", "", 0.0);
            }

        }).toList();
    }

    public List<DatosTextoJson> leerCsv(MultipartFile file) throws Exception {
        List<DatosTextoJson> lista = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] linea;
            while ((linea = reader.readNext()) != null) {
                // Suponiendo que el CSV tiene el texto en la primera columna
                lista.add(new DatosTextoJson(linea[0]));
            }
        }
        return lista;
    }

    ///


    public void escribirCsv(List<DatosRespuestaSentimiento> resultados, Writer writer) throws Exception {
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            // Cabeceras
            csvWriter.writeNext(new String[]{"Texto", "Prevision", "Probabilidad"});

            for (DatosRespuestaSentimiento res : resultados) {
                csvWriter.writeNext(new String[]{
                        res.texto(),
                        res.prevision(),
                        String.valueOf(res.probabilidad())
                });
            }
        }
    }

}




// ... dentro de SentimientoService ...


