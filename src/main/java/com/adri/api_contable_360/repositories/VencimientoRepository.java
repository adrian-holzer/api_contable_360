package com.adri.api_contable_360.repositories;

import com.adri.api_contable_360.models.Obligacion;
import com.adri.api_contable_360.models.Vencimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VencimientoRepository extends JpaRepository<Vencimiento, Long> {
    List<Vencimiento> findByObligacion(Obligacion obligacion);
    List<Vencimiento> findByObligacionAndTerminacionCuit(Obligacion obligacion, Integer terminacionCuit);
    List<Vencimiento> findByMesAndDia(Integer mes, Integer dia);
    // Puedes agregar más consultas personalizadas según tus necesidades





}