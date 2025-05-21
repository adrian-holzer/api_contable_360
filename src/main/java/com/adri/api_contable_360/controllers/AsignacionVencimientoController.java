package com.adri.api_contable_360.controllers;

import com.adri.api_contable_360.dto.AsignacionVencimientoModificacionDto;
import com.adri.api_contable_360.models.AsignacionVencimiento;
import com.adri.api_contable_360.repositories.AsignacionVencimientoRepository;
import com.adri.api_contable_360.services.AsignacionVencimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/asignaciones-vencimientos")
public class AsignacionVencimientoController {

    @Autowired
    private AsignacionVencimientoRepository asignacionVencimientoRepository;

    @Autowired
    private AsignacionVencimientoService asignacionVencimientoService;

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



    // AsignacionVencimiento por id

    @GetMapping("/{idAsignacionVencimiento}")
    public ResponseEntity<AsignacionVencimiento> obtenerAsignacionVencimientoPorId(@PathVariable Long idAsignacionVencimiento) {
        Optional<AsignacionVencimiento> asignacionVencimientoOptional = asignacionVencimientoService.findById(idAsignacionVencimiento);

        return asignacionVencimientoOptional.map(asignacionVencimiento -> new ResponseEntity<>(asignacionVencimiento, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{idAsignacionVencimiento}")
    public ResponseEntity<?> modificarAsignacionVencimiento(
            @PathVariable Long idAsignacionVencimiento,
            @RequestBody AsignacionVencimientoModificacionDto modificacionDto) {

        Optional<AsignacionVencimiento> resultado = asignacionVencimientoService.modificarAsignacionVencimiento(
                idAsignacionVencimiento,
                modificacionDto.getEstado(),
                modificacionDto.getObservacion()
        );

        return resultado.map(asignacionVencimiento -> new ResponseEntity<>(asignacionVencimiento, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



    @PostMapping("/enviar-notificacion")
    public ResponseEntity<?> enviarNotificacionConAdjuntos(
            @RequestParam("idAsignacionVencimiento") Long idAsignacionVencimiento,
            @RequestParam("idContactoDestinatario") Long idContactoDestinatario,
            @RequestParam(value = "observacion", required = false) String observacion,
            @RequestParam(value = "archivosAdjuntos", required = false) List<MultipartFile> archivosAdjuntos) {
        try {
            boolean enviado = asignacionVencimientoService.crearYEnviarNotificacion(
                    idAsignacionVencimiento,
                    idContactoDestinatario,
                    observacion,
                    archivosAdjuntos
            );
            if (enviado) {
                return ResponseEntity.ok("Notificación enviada correctamente.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar la notificación.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al enviar la notificación: " + e.getMessage());
        }
    }




    @GetMapping("/proximas-a-vencer")
    public ResponseEntity<List<AsignacionVencimiento>> listarAsignacionesProximasAVencer() {
        List<AsignacionVencimiento> asignacionesProximasAVencer = asignacionVencimientoService.getAsignacionesProximasAVencer();
        if (asignacionesProximasAVencer.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(asignacionesProximasAVencer, HttpStatus.OK);
    }
}