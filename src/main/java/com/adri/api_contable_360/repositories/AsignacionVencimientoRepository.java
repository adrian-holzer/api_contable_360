package com.adri.api_contable_360.repositories;

import com.adri.api_contable_360.models.Asignacion;
import com.adri.api_contable_360.models.AsignacionVencimiento;
import com.adri.api_contable_360.models.EstadoAsignacion;
import com.adri.api_contable_360.models.Vencimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsignacionVencimientoRepository extends JpaRepository<AsignacionVencimiento, Long> {
    List<AsignacionVencimiento> findByAsignacion(Asignacion asignacion);
    // Puedes agregar métodos de consulta específicos si los necesitas


    @Query("SELECT av FROM AsignacionVencimiento av " +
            "JOIN av.vencimiento v " +
            "JOIN av.asignacion a " +
            "JOIN a.cliente c " +
            "WHERE a.activo = true " + // Agregamos la condición para filtrar por activo = true
            "ORDER BY v.fechaVencimiento ASC")
    List<AsignacionVencimiento> findAllOrderByFechaVencimientoAsc();


    @Query("SELECT av FROM AsignacionVencimiento av " +
            "JOIN av.vencimiento v " +
            "JOIN av.asignacion a " +
            "JOIN a.cliente c " +
            "WHERE c.idCliente = :idCliente " +
            "ORDER BY v.fechaVencimiento ASC")
    List<AsignacionVencimiento> findByCliente_IdOrderByFechaVencimientoAsc(@Param("idCliente") Long idCliente);

    List<AsignacionVencimiento> findByVencimiento(Vencimiento vencimiento); // Nuevo método

    // MODIFICACIÓN AQUI: Agregando la condición de estado
    @Query("SELECT av FROM AsignacionVencimiento av " +
            "JOIN av.asignacion a "+
            "WHERE av.vencimiento.fechaVencimiento BETWEEN :hoy AND :diezDiasDespues " +
            "AND av.estado = :estadoPendiente AND a.activo = true" ) // Agrega la condición de estado
    List<AsignacionVencimiento> findProximasAVencer(
            @Param("hoy") LocalDate hoy,
            @Param("diezDiasDespues") LocalDate diezDiasDespues,
            @Param("estadoPendiente") EstadoAsignacion estadoPendiente // Nuevo parámetro


    );


}