package RichardLipa.sentimentAPI.controller;


import RichardLipa.sentimentAPI.domain.comentario.DatosListaComentarios;
import RichardLipa.sentimentAPI.domain.comentario.IComentarioRepository;
import RichardLipa.sentimentAPI.domain.comentario.Tipo;
import RichardLipa.sentimentAPI.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stats")// endPoind
public class StatisticsController {
    @Autowired
    private IComentarioRepository comentarioRepository;
    @Autowired
    private ComentarioService comentarioService;
    @GetMapping
    public ResponseEntity<Page<DatosListaComentarios>> listarComentarios(
            @PageableDefault(
                    size = 20,
                    sort = {"fechaRegistro"}, // Asegúrate que coincida con el nombre del atributo en tu entidad
                    direction = Sort.Direction.DESC,
                    page = 0
            )
            Pageable paginacion){
        var total = paginacion.getPageSize();

        //var page = comentarioRepository.findAllByStateTrue(paginacion).map(DatosListaComentarios:: new);
        System.out.println("ejecutando estadisticas..::::: "+ total);
        Page<DatosListaComentarios> page = comentarioService.obtenerComentariosPaginados(paginacion);
        //var listacomentarios = ResponseEntity.ok(page);
        return  ResponseEntity.ok(page);

    }



    @GetMapping("/listar")
    public ResponseEntity<?> listarComentarios2(
            @PageableDefault(size = 20, sort = {"fechaRegistro"}, direction = Sort.Direction.DESC) Pageable paginacion) {

        // 1. Obtener la página (por ejemplo, los 20 registros solicitados)
        Page<DatosListaComentarios> page = comentarioService.obtenerComentariosPaginados(paginacion);
        List<DatosListaComentarios> listaEnPagina = page.getContent();

        // 2. USO DE LAMBDA: Separar y contar en un solo paso
        Map<Boolean, Long> conteo = listaEnPagina.stream()
                .collect(Collectors.partitioningBy(
                        c -> c.prevision() == Tipo.POSITIVO,
                        Collectors.counting()
                ));

        long pos = conteo.get(true);
        long neg = conteo.get(false);
        int totalEnPagina = listaEnPagina.size();
        System.out.println("psoitvos :: "+ pos);
        System.out.println("negativos  :: "+ neg);

        // 3. Calcular porcentajes de la página actual
        String porcPos = (totalEnPagina > 0) ? (pos * 100 / totalEnPagina) + "%" : "0%";
        String porcNeg = (totalEnPagina > 0) ? (neg * 100 / totalEnPagina) + "%" : "0%";

        // 4. Construir respuesta JSON
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("total_en_pagina", totalEnPagina);
        respuesta.put("positivos", porcPos);
        respuesta.put("negativos", porcNeg);

        // Agregamos el resto de la estructura de Page
        respuesta.put("content", listaEnPagina);
        respuesta.put("totalElements", page.getTotalElements());
        respuesta.put("totalPages", page.getTotalPages());
        respuesta.put("number", page.getNumber());

        return ResponseEntity.ok(respuesta);
    }

}


