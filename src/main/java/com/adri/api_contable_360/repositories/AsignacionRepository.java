package com.adri.api_contable_360.repositories;

import com.adri.api_contable_360.models.Asignacion;
import com.adri.api_contable_360.models.Cliente;
import com.adri.api_contable_360.models.Obligacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    // Métodos adicionales si necesitas consultas personalizadas

    List<Asignacion> findByCliente(Cliente c);
    List<Asignacion> findByClienteAndObligacion(Cliente c, Obligacion o );
    List<Asignacion> findByClienteAndActivo(Cliente c,boolean activo);
    List<Asignacion> findByObligacion(Obligacion o);


    @Query("SELECT a FROM Asignacion a JOIN a.cliente c WHERE a.obligacion = :obligacion AND c.terminacionCuit = :terminacionCuit AND a.activo = true")
    List<Asignacion> findByObligacionAndClienteTerminacionCuitAndActivo(
            @Param("obligacion") Obligacion obligacion,
            @Param("terminacionCuit") Integer terminacionCuit
    );

}