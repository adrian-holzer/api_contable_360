package com.adri.api_contable_360.controllers;

import com.adri.api_contable_360.dto.ObligacionRequestDTO;
import com.adri.api_contable_360.dto.VencimientoDTO;
import com.adri.api_contable_360.models.*;
import com.adri.api_contable_360.repositories.AsignacionRepository;
import com.adri.api_contable_360.repositories.AsignacionVencimientoRepository;
import com.adri.api_contable_360.repositories.ObligacionRepository;
import com.adri.api_contable_360.repositories.VencimientoRepository;
import com.adri.api_contable_360.services.ObligacionService;
import com.adri.api_contable_360.services.VencimientoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/obligaciones")
public class ObligacionController {

    @Autowired
    private ObligacionService obligacionService;

    @Autowired
    private VencimientoService vencimientoService;


    @Autowired
    private ObligacionRepository obligacionRepository;

    @Autowired
    private AsignacionVencimientoRepository asignacionVencimientoRepository;

    @Autowired
    private VencimientoRepository vencimientoRepository;

    @Autowired
    private AsignacionRepository asignacionRepository;

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
    public ResponseEntity<List<Obligacion>> listarObligacionesActivas() {
        return ResponseEntity.ok(obligacionRepository.findByActivo(true));
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
    public ResponseEntity<?> crearObligacion(@RequestBody ObligacionRequestDTO obligacionRequest) {
        Obligacion nuevaObligacion = new Obligacion();
        nuevaObligacion.setNombre(obligacionRequest.getNombre());
        nuevaObligacion.setDescripcion(obligacionRequest.getDescripcion());
        nuevaObligacion.setObservaciones(obligacionRequest.getObservaciones());
        nuevaObligacion.setActivo(true);
        List<Vencimiento> vencimientos = new ArrayList<>();
        for (VencimientoDTO vencimientoDTO : obligacionRequest.getVencimientos()) {
            Integer anio = vencimientoDTO.getAnio() != null ? vencimientoDTO.getAnio() : Year.now().getValue();
            Integer mes = vencimientoDTO.getMes();
            Integer dia = vencimientoDTO.getDia();
            try {
                LocalDate fechaVencimiento = LocalDate.of(anio, mes, dia);
                Vencimiento vencimiento = new Vencimiento();
                vencimiento.setMes(mes);
                vencimiento.setTerminacionCuit(vencimientoDTO.getTerminacionCuit());
                vencimiento.setDia(dia);
                vencimiento.setAnio(anio);
                vencimiento.setObligacion(nuevaObligacion);
                vencimiento.setFechaVencimiento(fechaVencimiento);
                vencimientos.add(vencimiento);
            } catch (Exception e) {

                YearMonth yearMonth = YearMonth.of(anio, mes);
                int ultimoDiaDelMes = yearMonth.lengthOfMonth();
                if (dia > ultimoDiaDelMes) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("Día inválido para el mes y año : Año=" + anio + " Mes=" + mes + ", Día=" + dia + " (El último día del mes es: " + ultimoDiaDelMes + ")"); // Devuelve null en el body para indicar el error, o puedes crear un DTO para el error
                }
                else {


                    Vencimiento vencimiento = new Vencimiento();
                    vencimiento.setMes(mes);
                    vencimiento.setTerminacionCuit(vencimientoDTO.getTerminacionCuit());
                    vencimiento.setDia(dia);
                    vencimiento.setAnio(anio);
                    vencimiento.setObligacion(nuevaObligacion);
                    vencimiento.setFechaVencimiento(null);
                    vencimientos.add(vencimiento);
                }
            }
        }
        nuevaObligacion.setVencimientos(vencimientos);
        Obligacion obligacionCreada = obligacionService.crearObligacion(nuevaObligacion);
        return new ResponseEntity<>(obligacionCreada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarObligacion(@PathVariable Long id, @RequestBody ObligacionRequestDTO obligacionRequest) {
        Obligacion obligacionExistente = obligacionService.findById(id);

        if (obligacionExistente == null) {
            return new ResponseEntity<>("Obligacion no encontrada", HttpStatus.NOT_FOUND);
        }

        obligacionExistente.setNombre(obligacionRequest.getNombre());
        obligacionExistente.setDescripcion(obligacionRequest.getDescripcion());
        obligacionExistente.setObservaciones(obligacionRequest.getObservaciones());

        List<Vencimiento> vencimientosExistentes = obligacionExistente.getVencimientos();
        List<VencimientoDTO> vencimientosDTO = obligacionRequest.getVencimientos();
        List<Vencimiento> vencimientosActualizados = new ArrayList<>();

        // Actualizar o crear vencimientos
        for (VencimientoDTO vencimientoDTO : vencimientosDTO) {
            // Buscar si existe un vencimiento con la misma terminacionCuit y Mes
            Vencimiento vencimientoExistente = vencimientosExistentes.stream()
                    .filter(v -> v.getTerminacionCuit().equals(vencimientoDTO.getTerminacionCuit()) && v.getMes().equals(vencimientoDTO.getMes()))
                    .findFirst()
                    .orElse(null);

            if (vencimientoExistente != null) {
                // Actualizar el vencimiento existente
                vencimientoExistente.setDia(vencimientoDTO.getDia());
                vencimientosActualizados.add(vencimientoExistente);
            } else {
                // Crear un nuevo vencimiento
                Vencimiento nuevoVencimiento = new Vencimiento();
                nuevoVencimiento.setMes(vencimientoDTO.getMes());
                nuevoVencimiento.setTerminacionCuit(vencimientoDTO.getTerminacionCuit());
                nuevoVencimiento.setDia(vencimientoDTO.getDia());
                nuevoVencimiento.setObligacion(obligacionExistente);
                nuevoVencimiento.setAnio(Year.now().getValue());

                try {
                    nuevoVencimiento.setFechaVencimiento(LocalDate.of(nuevoVencimiento.getAnio(), nuevoVencimiento.getMes(), nuevoVencimiento.getDia()));
                } catch (Exception e) {
                    // Manejar casos de fechas inválidas (ej: día 31 en mes de 30, febrero 29 en año no bisiesto)
                    YearMonth yearMonth = YearMonth.of(vencimientoDTO.getAnio(), vencimientoDTO.getMes());
                    int ultimoDiaDelMes = yearMonth.lengthOfMonth();
                    if (vencimientoDTO.getDia() > ultimoDiaDelMes) {
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("Día inválido para el mes y año proporcionados en la obligación " + obligacionExistente.getNombre() +
                                        ": Año=" + vencimientoDTO.getAnio() + " Mes=" + vencimientoDTO.getMes() + ", Día=" + vencimientoDTO.getDia() + " (El último día del mes es: " + ultimoDiaDelMes + ")");
                    } else {
                        nuevoVencimiento.setFechaVencimiento(null);
                    }
                }
                vencimientoRepository.save(nuevoVencimiento);
                //vencimientosActualizados.add(nuevoVencimiento);



                // Buscar todas las asignaciones para esa obligacion y que la terminacion de cuit coincida con el cliente

                List<Asignacion> asignaciones = asignacionRepository.findByObligacionAndClienteTerminacionCuitAndActivo(obligacionExistente,vencimientoDTO.getTerminacionCuit());

                if (!asignaciones.isEmpty()) {
                    // Por cada asignacion crear una asignacion vencimientos asociado la asignacion con el nuevo vencimiento
                    for (Asignacion asignacion : asignaciones) {
                        AsignacionVencimiento asignacionVencimiento = new AsignacionVencimiento();
                        asignacionVencimiento.setAsignacion(asignacion);     // Asocia la asignación encontrada
                        asignacionVencimiento.setVencimiento(nuevoVencimiento); // Asocia el nuevo vencimiento guardado
                        asignacionVencimiento.setEstado(EstadoAsignacion.PENDIENTE); // Establece el estado inicial

                        // Puedes setear otras propiedades si es necesario, como observacion inicial, usuarioFinalizo, etc.
                        asignacionVencimientoRepository.save(asignacionVencimiento); // Guarda la nueva AsignacionVencimiento
                        System.out.println("Creada AsignacionVencimiento para Asignacion ID: " + asignacion.getIdAsignacion() +
                                " y Vencimiento ID: " + nuevoVencimiento.getId());
                    }
                } else {
                    System.out.println("No se encontraron asignaciones activas para la obligación " + obligacionExistente.getNombre() +
                            " y terminación CUIT " + vencimientoDTO.getTerminacionCuit());
                }






            }
        }

        // Eliminar los vencimientos que ya no están en la lista del DTO
        List<Vencimiento> vencimientosAEliminar = new ArrayList<>();
        Iterator<Vencimiento> iterator = vencimientosExistentes.iterator(); // Usar un iterador
        while (iterator.hasNext()) {
            Vencimiento vencimiento = iterator.next();
            boolean encontradoEnDTO = false;
            for (VencimientoDTO dto : vencimientosDTO) {
                if (vencimiento.getTerminacionCuit().equals(dto.getTerminacionCuit()) &&
                        vencimiento.getMes().equals(dto.getMes()) &&
                        (dto.getAnio() == null || dto.getAnio().equals(vencimiento.getAnio()))) {
                    encontradoEnDTO = true;
                    break;
                }
            }
            if (!encontradoEnDTO) {
                vencimientosAEliminar.add(vencimiento);
                iterator.remove(); // Eliminar de la lista existente

                // Eliminar las asignaciones de vencimiento asociadas
                List<AsignacionVencimiento> asignacionesVencimiento = asignacionVencimientoRepository.findByVencimiento(vencimiento);
                for (AsignacionVencimiento av : asignacionesVencimiento) {
                    asignacionVencimientoRepository.delete(av);
                }
                vencimientoRepository.deleteById(vencimiento.getId()); // Eliminar de la base de datos
            }
        }


        // Calcular y setear la fecha de vencimiento
        for (Vencimiento vencimiento : vencimientosActualizados) {
            Integer anio = vencimiento.getAnio() != null ? vencimiento.getAnio() : Year.now().getValue();
            Integer mes = vencimiento.getMes();
            Integer dia = vencimiento.getDia();
            try {
                vencimiento.setFechaVencimiento(LocalDate.of(anio, mes, dia));
            } catch (Exception e) {
                // Manejar casos de fechas inválidas (ej: día 31 en mes de 30, febrero 29 en año no bisiesto)
                YearMonth yearMonth = YearMonth.of(anio, mes);
                int ultimoDiaDelMes = yearMonth.lengthOfMonth();
                if (dia > ultimoDiaDelMes) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("Día inválido para el mes y año proporcionados en la obligación " + obligacionExistente.getNombre() +
                                    ": Año=" + anio + " Mes=" + mes + ", Día=" + dia + " (El último día del mes es: " + ultimoDiaDelMes + ")");
                } else {
                    vencimiento.setFechaVencimiento(null);
                }
            }
        }
        obligacionExistente.setVencimientos(vencimientosActualizados); // Actualizar la lista de vencimientos.
        Obligacion obligacionActualizada = obligacionService.crearObligacion(obligacionExistente);
        return new ResponseEntity<>(obligacionActualizada, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @Transactional // Para manejar la transacción de forma declarativa
    public ResponseEntity<String> eliminarObligacion(@PathVariable Long id) {
        Optional<Obligacion> obligacionOptional = obligacionRepository.findById(id);

        if (!obligacionOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la obligación con ID: " + id);
        }

        Obligacion obligacion = obligacionOptional.get();

        // Verificar si existen asignaciones asociadas
        List<Asignacion> asignaciones = asignacionRepository.findByObligacion(obligacion); // Usa el nombre correcto de la propiedad

        if (!asignaciones.isEmpty()) {
            // Si hay asignaciones, desactivarlas
            for (Asignacion asignacion : asignaciones) {
                asignacion.setActivo(false);
                asignacionRepository.save(asignacion);
            }
            // Desactivar la obligación
            obligacion.setActivo(false);
            obligacionRepository.save(obligacion);
            return ResponseEntity.ok("Obligación desactivada y asignaciones asociadas inactivadas.");
        } else {
            // Si no hay asignaciones, eliminar la obligación y sus vencimientos
            // Primero eliminamos los vencimientos asociados a la obligación
            vencimientoRepository.deleteByObligacion(obligacion); // Asegúrate de que este método exista en tu VencimientoRepository
            obligacionRepository.delete(obligacion);
            return ResponseEntity.ok("Obligación y vencimientos eliminados.");
        }
    }

}