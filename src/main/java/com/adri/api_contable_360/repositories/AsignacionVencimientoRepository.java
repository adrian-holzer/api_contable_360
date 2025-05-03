package com.adri.api_contable_360.repositories;

import com.adri.api_contable_360.models.Asignacion;
import com.adri.api_contable_360.models.AsignacionVencimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionVencimientoRepository extends JpaRepository<AsignacionVencimiento, Long> {
    List<AsignacionVencimiento> findByAsignacion(Asignacion asignacion);
    // Puedes agregar métodos de consulta específicos si los necesitas


    @Query("SELECT av FROM AsignacionVencimiento av " +
            "JOIN av.vencimiento v " +
            "JOIN av.asignacion a " +
            "JOIN a.cliente c " +
            "ORDER BY v.fechaVencimiento ASC")
    List<AsignacionVencimiento> findAllOrderByFechaVencimientoAsc();

    @Query("SELECT av FROM AsignacionVencimiento av " +
            "JOIN av.vencimiento v " +
            "JOIN av.asignacion a " +
            "JOIN a.cliente c " +
            "WHERE c.idCliente = :idCliente " +
            "ORDER BY v.fechaVencimiento ASC")
    List<AsignacionVencimiento> findByCliente_IdOrderByFechaVencimientoAsc(@Param("idCliente") Long idCliente);


}