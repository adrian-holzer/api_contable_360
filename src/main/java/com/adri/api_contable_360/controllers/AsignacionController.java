package com.adri.api_contable_360.controllers;

import com.adri.api_contable_360.dto.AsignacionDto;
import com.adri.api_contable_360.models.Asignacion;
import com.adri.api_contable_360.models.Cliente;
import com.adri.api_contable_360.models.Obligacion;
import com.adri.api_contable_360.models.Vencimiento;
import com.adri.api_contable_360.services.AsignacionService;
import com.adri.api_contable_360.services.ClienteService;
import com.adri.api_contable_360.services.ObligacionService;
import com.adri.api_contable_360.services.VencimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/asignaciones")
public class AsignacionController {



    @Autowired
    private ObligacionService obligacionService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private VencimientoService vencimientoService;

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
    public ResponseEntity<?> crearAsignaciones(@RequestBody AsignacionDto asignacionRequest) {
        Cliente cliente = clienteService.findById(asignacionRequest.getIdCliente());

        if (cliente == null) {
            return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
        }
        System.out.println(asignacionRequest);

        List<Long> idsObligaciones = asignacionRequest.getIdsObligaciones();
        List<Asignacion> asignacionesCreadas = new ArrayList<>();
        List<String> errores = new ArrayList<>();

        // Obtener todas las asignaciones existentes para este cliente
        List<Asignacion> asignacionesExistentesCliente = asignacionService.findByCliente(cliente);

        // Crear un conjunto con los IDs de las obligaciones que SÍ se deben asignar (activas)
        Set<Long> obligacionesAActivar = new HashSet<>(idsObligaciones);

        // Iterar sobre las asignaciones existentes del cliente para actualizar el campo activo
        for (Asignacion asignacionExistente : asignacionesExistentesCliente) {
            if (obligacionesAActivar.contains(asignacionExistente.getObligacion().getId())) {
                // La obligación todavía está seleccionada, se mantiene activa
                asignacionExistente.setActivo(true);
                asignacionService.save(asignacionExistente);
                obligacionesAActivar.remove(asignacionExistente.getObligacion().getId()); // Para que no se creen duplicados
            } else {
                // La obligación ya no está seleccionada, se desactiva
                asignacionExistente.setActivo(false);
                asignacionService.save(asignacionExistente);
            }
        }


        // Crear las nuevas asignaciones para las obligaciones que NO existían previamente
        for (Long idObligacion : obligacionesAActivar) {
            Obligacion obligacion = obligacionService.findById(idObligacion);
            if (obligacion != null) {
                Asignacion nuevaAsignacion = new Asignacion();
                nuevaAsignacion.setCliente(cliente);
                nuevaAsignacion.setObligacion(obligacion);
                nuevaAsignacion.setObservacion(asignacionRequest.getObservacion());
                nuevaAsignacion.setActivo(true);
                Asignacion asignacionGuardada = asignacionService.save(nuevaAsignacion);
                asignacionesCreadas.add(asignacionGuardada);
                List<Vencimiento> vencimientos = vencimientoService.obtenerVencimientosPorObligacionYTerminacionCuit(
                        asignacionGuardada.getIdAsignacion()
                );
                asignacionService.crearAsignacionesVencimiento(asignacionGuardada, vencimientos);
            } else {
                errores.add("No se encontró la obligación con ID: " + idObligacion);
            }
        }

        if (!errores.isEmpty() && asignacionesCreadas.isEmpty()) {
            return new ResponseEntity<>(errores, HttpStatus.CONFLICT);
        } else if (!errores.isEmpty()) {
            return new ResponseEntity<>(errores, HttpStatus.MULTI_STATUS);
        } else {
            return new ResponseEntity<>(asignacionesCreadas, HttpStatus.CREATED);
        }
    }







    @PutMapping("/{id}")
    public ResponseEntity<Asignacion> updateAsignacion(@PathVariable Long id, @RequestBody Asignacion asignacion) {
        Optional<Asignacion> existingAsignacion = asignacionService.getAsignacionById(id);
        if (existingAsignacion.isPresent()) {
            asignacion.setIdAsignacion(id);
            Asignacion updatedAsignacion = asignacionService.save(asignacion);
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




    @GetMapping("/{idAsignacion}/vencimientos")
    public ResponseEntity<?>  vencimientoPorAsignacion(@PathVariable Long idAsignacion) {
        List <Vencimiento> vencimientos = vencimientoService.obtenerVencimientosPorObligacionYTerminacionCuit(idAsignacion);

        return new ResponseEntity<>(vencimientos, HttpStatus.OK);

    }



    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<?> asignacionesPorCliente(@PathVariable Long idCliente) {
        List <Asignacion> asignaciones = asignacionService.findByClienteAndActivo(idCliente);

        return new ResponseEntity<>(asignaciones, HttpStatus.OK);

    }
}