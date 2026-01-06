package RichardLipa.sentimentAPI.controller;


import RichardLipa.sentimentAPI.domain.comentario.DatosRespuestaSentimiento;
import RichardLipa.sentimentAPI.domain.comentario.DatosTextoJson;
import RichardLipa.sentimentAPI.service.SentimientoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/predict")// endPoind
public class PredictController {

    @Autowired
    private SentimientoService service;

    // 1. MANTIENE LA FUNCIONALIDAD JSON ORIGINAL
    @PostMapping(consumes = "application/json")
    public ResponseEntity<List<DatosRespuestaSentimiento>> analizarJson(@RequestBody List<DatosTextoJson> datos) {
        System.out.println("ejecutando /predict:: " + datos.size());
        List<DatosRespuestaSentimiento> resultados = service.procesarLista(datos);
        return ResponseEntity.ok(resultados);
    }

    // 2. RECIBE UN ARCHIVO CSV Y DEVUELVE JSON
    @PostMapping(value = "/upload-csv", consumes = "multipart/form-data")
    public ResponseEntity<List<DatosRespuestaSentimiento>> analizarCsv(@RequestParam("file") MultipartFile file) throws Exception {
        // Leemos el CSV y lo convertimos a la lista de objetos que ya conoces
        List<DatosTextoJson> datos = service.leerCsv(file);
        List<DatosRespuestaSentimiento> resultados = service.procesarLista(datos);
        return ResponseEntity.ok(resultados);
    }

    // 3. RECIBE UN ARCHIVO CSV Y DEVUELVE OTRO ARCHIVO CSV (EXPORTAR)
    @PostMapping(value = "/export-csv", consumes = "multipart/form-data", produces = "text/csv")
    public void exportarCsv(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
        // Configuramos el nombre del archivo de salida
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=resultados_sentimiento.csv");

        List<DatosTextoJson> datos = service.leerCsv(file);
        List<DatosRespuestaSentimiento> resultados = service.procesarLista(datos);

        // Llamamos al servicio para escribir los resultados directamente en el flujo de respuesta
        service.escribirCsv(resultados, response.getWriter());
    }
}
