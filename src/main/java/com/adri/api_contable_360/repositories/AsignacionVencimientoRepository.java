package com.adri.api_contable_360.repositories;

import com.adri.api_contable_360.models.Asignacion;
import com.adri.api_contable_360.models.AsignacionVencimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionVencimientoRepository extends JpaRepository<AsignacionVencimiento, Long> {
    List<AsignacionVencimiento> findByAsignacion(Asignacion asignacion);
    // Puedes agregar métodos de consulta específicos si los necesitas
}