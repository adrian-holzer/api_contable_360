package com.adri.api_contable_360.controllers;

import com.adri.api_contable_360.models.Asignacion;
import com.adri.api_contable_360.services.AsignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/asignaciones")
public class AsignacionController {

    @Autowired
    private AsignacionService asignacionService;

    @GetMapping
    public ResponseEntity<List<Asignacion>> getAllAsignaciones() {
        List<Asignacion> asignaciones = asignacionService.getAllAsignaciones();
        return new ResponseEntity<>(asignaciones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asignacion> getAsignacionById(@PathVariable Long id) {
        Optional<Asignacion> asignacion = asignacionService.getAsignacionById(id);
        return asignacion.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Asignacion> createAsignacion(@RequestBody Asignacion asignacion) {
        Asignacion savedAsignacion = asignacionService.saveAsignacion(asignacion);
        return new ResponseEntity<>(savedAsignacion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Asignacion> updateAsignacion(@PathVariable Long id, @RequestBody Asignacion asignacion) {
        Optional<Asignacion> existingAsignacion = asignacionService.getAsignacionById(id);
        if (existingAsignacion.isPresent()) {
            asignacion.setIdAsignacion(id);
            Asignacion updatedAsignacion = asignacionService.saveAsignacion(asignacion);
            return new ResponseEntity<>(updatedAsignacion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAsignacion(@PathVariable Long id) {
        asignacionService.deleteAsignacion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}