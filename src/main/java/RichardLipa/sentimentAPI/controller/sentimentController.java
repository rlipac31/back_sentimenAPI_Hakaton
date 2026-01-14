package RichardLipa.sentimentAPI.controller;


import RichardLipa.sentimentAPI.domain.comentario.*;
import RichardLipa.sentimentAPI.service.SentimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/sentiment")// endPoind
public class sentimentController {

    // guardando en bd
    @Autowired
    private IComentarioRepository repository;
    @Autowired
    private SentimientoService service;

    // 1. MANTIENE LA FUNCIONALIDAD JSON ORIGINAL
    @PostMapping
    @Transactional // Asegura que se guarde todo o nada
    public ResponseEntity<?> registrarComentarios(@RequestBody(required = false) List<DatosRegistroComentario> datos) {
        if (datos == null || datos.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMensaje("Lista vacía", 400));
        }
        try {
                List<DatosRespuestaSentimiento> resultados = service.procesarLista(datos);
                // --- VALIDACIÓN AQUÍ ---
                if (resultados == null || resultados.isEmpty()) {
                    return ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ErrorMensaje("El servicio de análisis no devolvió resultados", 500));
                }
                // llamando al metodo de servicio guardando dados en bd
                service.guardarEnBaseDeDatos(resultados);
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            // Si la API en la nube falla o hay error de red, cae aquí
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ErrorMensaje("La IA en la nube no responde: " + e.getMessage(), 503));
        }

    }

   @PostMapping(value = "/upload-csv", consumes = "multipart/form-data")
    public ResponseEntity<?> analizarCsv(@RequestParam("file") MultipartFile file) {
        System.out.println("ejecutando /sentiment/upload-csv");
        try {
            // 1. Validar si el archivo existe
            if (file == null || file.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorMensaje("No se ha seleccionado ningún archivo.", 400));
            }
            // 2. VALIDACIÓN DE TAMAÑO (Máximo 3MB)
            // 3MB = 3 * 1024 * 1024 bytes = 3,145,728 bytes
            long maxSizeBytes = 3 * 1024 * 1024;
            if (file.getSize() > maxSizeBytes) {
                return ResponseEntity
                        .status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body(new ErrorMensaje("El archivo es demasiado grande. El límite permitido es de 3MB.", 413));
            }

            // 3. Validar extensión CSV
            String contentType = file.getContentType();
            String fileName = file.getOriginalFilename();
            boolean isCsv = (contentType != null && contentType.equals("text/csv")) ||
                    (fileName != null && fileName.toLowerCase().endsWith(".csv"));

            if (!isCsv) {
                return ResponseEntity
                        .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body(new ErrorMensaje("Formato no válido. Solo se permiten archivos .csv", 415));
            }

            // 4. Procesar datos
            List<DatosRegistroComentario> datosTexto = service.leerCsv(file);
            if (datosTexto.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorMensaje("El archivo CSV no contiene datos válidos.", 400));
            }

            List<DatosRespuestaSentimiento> resultados = service.procesarLista(datosTexto);
            // --- VALIDACIÓN AQUÍ ---
            if (resultados == null || resultados.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorMensaje("El servicio de análisis no devolvió resultados", 500));
            }

            // Convertir DTOs a Entidades
            service.guardarEnBaseDeDatos(resultados);
            return ResponseEntity.ok(resultados);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMensaje("Error interno al procesar el CSV: " + e.getMessage(), 500));
        }
    }

    @PostMapping(value = "/export-csv", consumes = "application/json")// tiene que ser POST porque get tiene limitacion de escritra de caracteres(con PostMaapping solo capturamos la informacion que el usuario eta veindo en su pantalla:los comentarios lsitados)
    public void exportarCsv(@RequestBody List<DatosRespuestaSentimiento> resultados, HttpServletResponse response) throws Exception {
        // 1. Configurar los headers de la respuesta para descarga de archivo
        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=resultados_sentimiento.csv");

        // 2. Llamamos al servicio para escribir los resultados directamente en el flujo de respuesta
        // Usamos el writer de la respuesta (HttpServletResponse)
        service.escribirCsv(resultados, response.getWriter());
    }

    // 3. RECIBE UN ARCHIVO CSV Y DEVUELVE OTRO ARCHIVO CSV (EXPORTAR)
/*
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
*/
    /// ////@PostMapping(value = "/export-csv", consumes = "application/json", produces = "text/csv")
    /// public void exportarCsv(@RequestBody List<DatosRespuestaSentimiento> resultados, HttpServletResponse response) throws Exception {
    ///     // 1. Configurar headers para la descarga del archivo
    ///     response.setContentType("text/csv");
    ///     response.setCharacterEncoding("UTF-16"); // Recomendado para tildes y caracteres especiales
    ///     response.setHeader("Content-Disposition", "attachment; filename=resultados_sentimiento.csv");
    ///
    ///     // 2. Llamar al servicio para escribir el CSV usando el Writer de la respuesta
    ///     // No necesitamos leer archivos ni procesar, ya recibimos los "resultados" listos.
    ///     service.escribirCsv(resultados, response.getWriter());
    /// }




}
