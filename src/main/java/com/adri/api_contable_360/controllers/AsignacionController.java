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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        List<Long> idsObligaciones = asignacionRequest.getIdsObligaciones();
        List<Asignacion> asignacionesCreadas = new ArrayList<>();
        List<String> errores = new ArrayList<>();

        for (Long idObligacion : idsObligaciones) {
            Obligacion obligacion = obligacionService.findById(idObligacion);
            if (obligacion != null) {
                // Verificar si ya existe una asignación para este cliente y obligación
                List<Asignacion> asignacionesExistentes = asignacionService.findByClienteIdAndObligacionId(
                        cliente,
                        obligacion
                );

                if (asignacionesExistentes.isEmpty()) {
                    Asignacion nuevaAsignacion = new Asignacion();
                    nuevaAsignacion.setCliente(cliente);
                    nuevaAsignacion.setObligacion(obligacion);
                    nuevaAsignacion.setObservacion(asignacionRequest.getObservacion());

                    Asignacion asignacionGuardada = asignacionService.save(nuevaAsignacion);
                    asignacionesCreadas.add(asignacionGuardada);

                    // Crear las AsignacionVencimiento para esta asignación
                    // Asumo que tienes un método en VencimientoService para esto
                    List<Vencimiento> vencimientos = vencimientoService.obtenerVencimientosPorObligacionYTerminacionCuit(
                            asignacionGuardada.getIdAsignacion() // Aquí deberías usar los IDs de Obligacion y Cliente
                    );
                    asignacionService.crearAsignacionesVencimiento(asignacionGuardada, vencimientos);

                } else {
                    errores.add("Ya existe una asignación para el Cliente ID: " + cliente.getIdCliente() +
                            " y la Obligación ID: " + obligacion.getId());
                }
            } else {
                errores.add("No se encontró la obligación con ID: " + idObligacion);
            }
        }

        if (!errores.isEmpty() && asignacionesCreadas.isEmpty()) {
            return new ResponseEntity<>(errores, HttpStatus.CONFLICT);
        } else if (!errores.isEmpty()) {
            // Si se crearon algunas asignaciones pero hubo duplicados
            return new ResponseEntity<>(HttpStatus.MULTI_STATUS); // Puedes personalizar el cuerpo de la respuesta si lo deseas
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



}