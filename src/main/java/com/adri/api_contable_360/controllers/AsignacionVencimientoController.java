package com.adri.api_contable_360.controllers;

import com.adri.api_contable_360.models.AsignacionVencimiento;
import com.adri.api_contable_360.repositories.AsignacionVencimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/asignaciones-vencimientos")
public class AsignacionVencimientoController {

    @Autowired
    private AsignacionVencimientoRepository asignacionVencimientoRepository;

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<AsignacionVencimiento>> listarTodosLasAsignacionesVencimientosPorCliente(@PathVariable Long idCliente) {
        List<AsignacionVencimiento> listAsignacionVencimientos = asignacionVencimientoRepository.findByCliente_IdOrderByFechaVencimientoAsc(idCliente);

        if (listAsignacionVencimientos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // O HttpStatus.NOT_FOUND si prefieres
        }

        return new ResponseEntity<>(listAsignacionVencimientos, HttpStatus.OK);
    }

    @GetMapping // Endpoint para listar todas las asignaciones de vencimiento (ordenadas)
    public ResponseEntity<List<AsignacionVencimiento>> listarTodasLasAsignacionesVencimientos() {
        List<AsignacionVencimiento> listAsignacionVencimientos = asignacionVencimientoRepository.findAllOrderByFechaVencimientoAsc();
        return new ResponseEntity<>(listAsignacionVencimientos, HttpStatus.OK);
    }

}