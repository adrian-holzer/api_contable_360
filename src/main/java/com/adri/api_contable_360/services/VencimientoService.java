package com.adri.api_contable_360.services;


import com.adri.api_contable_360.models.Asignacion;
import com.adri.api_contable_360.models.Obligacion;
import com.adri.api_contable_360.models.Vencimiento;
import com.adri.api_contable_360.repositories.AsignacionRepository;
import com.adri.api_contable_360.repositories.ObligacionRepository;
import com.adri.api_contable_360.repositories.VencimientoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VencimientoService {


    @Autowired
    private ObligacionRepository obligacionRepository;

    @Autowired
    private VencimientoRepository vencimientoRepository;
    @Autowired
    private AsignacionRepository asignacionRepository;



    public Vencimiento findById(Long id) {
        return vencimientoRepository.findById(id).orElse(null);
    }

    @Transactional
    public void eliminarVencimientosPorObligacion(Obligacion obligacion) {
        vencimientoRepository.deleteByObligacion(obligacion);
    }


    public List<Vencimiento> obtenerVencimientosPorObligacionYTerminacionCuit(Long asignacionId) {
        Optional<Asignacion> asignacionOptional = asignacionRepository.findById(asignacionId);
        if (asignacionOptional.isPresent()) {
            Asignacion asignacion = asignacionOptional.get();
            Integer terminacionCuitCliente = asignacion.getCliente().getTerminacionCuit();
            return vencimientoRepository.findByObligacionAndTerminacionCuit(asignacion.getObligacion(), terminacionCuitCliente);
        }
        return List.of(); // Devuelve una lista vacía si la asignación no se encuentra
    }




}
