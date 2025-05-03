package com.adri.api_contable_360.controllers;


import com.adri.api_contable_360.models.AsignacionVencimiento;
import com.adri.api_contable_360.models.Obligacion;
import com.adri.api_contable_360.models.Vencimiento;
import com.adri.api_contable_360.repositories.AsignacionVencimientoRepository;
import com.adri.api_contable_360.repositories.ObligacionRepository;
import com.adri.api_contable_360.repositories.VencimientoRepository;
import com.adri.api_contable_360.services.ObligacionService;
import com.adri.api_contable_360.services.VencimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vencimientos")
public class VencimientoController {


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




    @GetMapping("{idVencimento}")
    public ResponseEntity<?>  vencimientoPorId(@PathVariable Long idVencimiento) {
        Vencimiento vencimiento = vencimientoService.findById(idVencimiento);

        if (vencimiento != null) {
            return new ResponseEntity<>(vencimiento, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Vencimiento no encontrado con ID: " + idVencimiento, HttpStatus.NOT_FOUND);
        }
    }



    // Vencimientos por Asignacion

    // Vencimientos por Cliente


    // Todos los Vencimeintos


    @GetMapping("")
    public ResponseEntity<?>  listarTodosLasAsignacionesVencimientos() {
        List<AsignacionVencimiento> listAsignacionVencimientos = asignacionVencimientoRepository.findAll();

            return new ResponseEntity<>(listAsignacionVencimientos, HttpStatus.OK);
        }




}
