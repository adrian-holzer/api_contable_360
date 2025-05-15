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
        System.out.println(asignacionRequest);

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
                    nuevaAsignacion.setActivo(true);

                    Asignacion asignacionGuardada = asignacionService.save(nuevaAsignacion);
                    asignacionesCreadas.add(asignacionGuardada);

                    // Crear las AsignacionVencimiento para esta asignación
                    // Asumo que tienes un método en VencimientoService para esto
                    List<Vencimiento> vencimientos = vencimientoService.obtenerVencimientosPorObligacionYTerminacionCuit(
                            asignacionGuardada.getIdAsignacion() // Aquí deberías usar los IDs de Obligacion y Cliente
                    );
                    asignacionService.crearAsignacionesVencimiento(asignacionGuardada, vencimientos);

                } else {
                    asignacionesExistentes.get(0).setActivo(true);
                    asignacionService.save(asignacionesExistentes.get(0));

                }
            } else {
                errores.add("No se encontró la obligación con ID: " + idObligacion);
            }
        }

        if (!errores.isEmpty() && asignacionesCreadas.isEmpty()) {
            return new ResponseEntity<>(errores, HttpStatus.CONFLICT);
        } else if (!errores.isEmpty()) {
            // Si se crearon algunas asignaciones pero hubo duplicados
            return new ResponseEntity<>(errores,HttpStatus.MULTI_STATUS); // Puedes personalizar el cuerpo de la respuesta si lo deseas
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