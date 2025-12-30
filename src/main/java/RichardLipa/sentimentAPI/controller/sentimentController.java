package RichardLipa.sentimentAPI.controller;


import RichardLipa.sentimentAPI.domain.comentario.DatosRespuestaSentimiento;
import RichardLipa.sentimentAPI.domain.comentario.DatosTextoJson;
import RichardLipa.sentimentAPI.domain.comentario.ListaComentariosRecord;
import RichardLipa.sentimentAPI.service.SentimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<List<DatosRespuestaSentimiento>>  analizar(@RequestBody List<DatosTextoJson> datos) {
        // 1. Recibimos la lista de Java Records
        // 2. El servicio los manda uno por uno al Colab
        List<DatosRespuestaSentimiento> resultados = service.procesarLista(datos);
        System.out.println("Api ...recibiendo comentarios....");
        //System.out.println(resultados);
      //  return ResponseEntity.ok(datos.resenias());
        // 3. Devolvemos la lista de predicciones al usuario
        return ResponseEntity.ok(resultados);
    }

}


