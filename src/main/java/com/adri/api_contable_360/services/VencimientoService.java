package com.adri.api_contable_360.services;


import com.adri.api_contable_360.models.Obligacion;
import com.adri.api_contable_360.models.Vencimiento;
import com.adri.api_contable_360.repositories.ObligacionRepository;
import com.adri.api_contable_360.repositories.VencimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VencimientoService {


    @Autowired
    private ObligacionRepository obligacionRepository;

    @Autowired
    private VencimientoRepository vencimientoRepository;



    public Vencimiento findById(Long id) {
        return vencimientoRepository.findById(id).orElse(null);
    }






}
