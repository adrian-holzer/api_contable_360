package com.adri.api_contable_360.services;

import com.adri.api_contable_360.models.*;
import com.adri.api_contable_360.repositories.AsignacionRepository;
import com.adri.api_contable_360.repositories.AsignacionVencimientoRepository;
import com.adri.api_contable_360.repositories.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AsignacionService {

    @Autowired
    private AsignacionRepository asignacionRepository;


    @Autowired
    private AsignacionVencimientoRepository asignacionVencimientoRepository;


    @Autowired
    private ClienteRepository clienteRepository;

    public List<Asignacion> getAllAsignaciones() {
        return asignacionRepository.findAll();
    }

    public Optional<Asignacion> getAsignacionById(Long id) {
        return asignacionRepository.findById(id);
    }

    public Asignacion save(Asignacion asignacion) {
        return asignacionRepository.save(asignacion);
    }


    @Transactional
    public void crearAsignacionesVencimiento(Asignacion asignacion, List<Vencimiento> vencimientos) {
        for (Vencimiento vencimiento : vencimientos) {
            AsignacionVencimiento asignacionVencimiento = new AsignacionVencimiento();
            asignacionVencimiento.setAsignacion(asignacion);
            asignacionVencimiento.setVencimiento(vencimiento);
            asignacionVencimientoRepository.save(asignacionVencimiento);
        }
    }

    // El método crearAsignacionesParaCliente ya no es estrictamente necesario como estaba antes
    // pero podría mantenerse si tienes otros casos de uso.
    // Aquí lo modificamos para que solo guarde la asignación.
    public Asignacion crearAsignacionParaCliente(Asignacion asignacion) {
        return asignacionRepository.save(asignacion);
    }

    public List<AsignacionVencimiento> getVencimientosDeAsignacion(Long asignacionId) {

        Asignacion a = asignacionRepository.findById(asignacionId).orElse(null);

        return asignacionVencimientoRepository.findByAsignacion(a);
    }


    public List<Asignacion> findByClienteIdAndObligacionId(Cliente c, Obligacion o) {

        return asignacionRepository.findByClienteAndObligacion(c, o);
    }

    public void deleteAsignacion(Long id) {
        asignacionRepository.deleteById(id);
    }



    public List<Asignacion> findByClienteAndActivo(Long idCliente) {

        Cliente c = clienteRepository.findById(idCliente).orElse(null);

        if (c!=null){
            return asignacionRepository.findByClienteAndActivo(c,true);

        }else return null;
    }

}