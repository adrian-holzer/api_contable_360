package com.adri.api_contable_360.repositories;

import com.adri.api_contable_360.models.Obligacion;
import com.adri.api_contable_360.models.Vencimiento;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VencimientoRepository extends JpaRepository<Vencimiento, Long> {
    List<Vencimiento> findByObligacion(Obligacion obligacion);
    List<Vencimiento> findByObligacionAndTerminacionCuit(Obligacion obligacion, Integer terminacionCuit);
    List<Vencimiento> findByMesAndDia(Integer mes, Integer dia);


    void deleteByObligacion(Obligacion obligacion); // Nuevo método para eliminar por la entidad Obligacion


    // Puedes agregar más consultas personalizadas según tus necesidades





}