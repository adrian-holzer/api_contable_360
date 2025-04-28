package com.adri.api_contable_360.controllers;

import com.adri.api_contable_360.dto.ObligacionRequestDTO;
import com.adri.api_contable_360.models.Obligacion;
import com.adri.api_contable_360.models.Vencimiento;
import com.adri.api_contable_360.repositories.ObligacionRepository;
import com.adri.api_contable_360.repositories.VencimientoRepository;
import com.adri.api_contable_360.services.ObligacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/obligaciones")
public class ObligacionController {

    @Autowired
    private ObligacionService obligacionService;

    @Autowired
    private ObligacionRepository obligacionRepository;

    @Autowired
    private VencimientoRepository vencimientoRepository;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Por favor, selecciona un archivo.");
        }
        try {
            obligacionService.procesarExcel(file);
            return ResponseEntity.ok("Archivo Excel procesado correctamente.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el archivo Excel: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Obligacion>> listarObligaciones() {
        return ResponseEntity.ok(obligacionRepository.findAll());
    }


    @GetMapping("{idObligacion}/vencimientos")
    public ResponseEntity<List<Vencimiento>> listarVencimientosPorObligacion(@PathVariable Long idObligacion) {

        return ResponseEntity.ok(obligacionService.listarVencimientosPorObligacion(idObligacion));
    }



    @GetMapping("/{idObligacion}")
    public ResponseEntity<?> obtenerObligacionPorId(@PathVariable Long idObligacion) {
        Obligacion obligacion = obligacionService.findById(idObligacion);
        if (obligacion != null) {
            return new ResponseEntity<>(obligacion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Obligación no encontrada con ID: " + idObligacion, HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("")
    public ResponseEntity<Obligacion> crearObligacion(@RequestBody ObligacionRequestDTO obligacionRequest) {
        Obligacion nuevaObligacion = new Obligacion();
        nuevaObligacion.setNombre(obligacionRequest.getNombre());
        nuevaObligacion.setDescripcion(obligacionRequest.getDescripcion());
        nuevaObligacion.setObservaciones(obligacionRequest.getObservacionesObligacion());

        List<Vencimiento> vencimientos = obligacionRequest.getVencimientos().stream()
                .map(vencimientoDTO -> {
                    Vencimiento vencimiento = new Vencimiento();
                    vencimiento.setMes(vencimientoDTO.getMes());
                    vencimiento.setTerminacionCuit(vencimientoDTO.getTerminacionCuit());
                    vencimiento.setDia(vencimientoDTO.getDia());
                    vencimiento.setObligacion(nuevaObligacion);
                    return vencimiento;
                }).collect(Collectors.toList());

        nuevaObligacion.setVencimientos(vencimientos);

        Obligacion obligacionCreada = obligacionService.crearObligacion(nuevaObligacion);
        return new ResponseEntity<>(obligacionCreada, HttpStatus.CREATED);
    }









}