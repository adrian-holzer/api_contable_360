package com.adri.api_contable_360.repositories;

import com.adri.api_contable_360.models.Asignacion;
import com.adri.api_contable_360.models.Cliente;
import com.adri.api_contable_360.models.Obligacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    // Métodos adicionales si necesitas consultas personalizadas


    List<Asignacion> findByClienteAndObligacionAndActivo(Cliente c, Obligacion o ,boolean estado);
    List<Asignacion> findByClienteAndActivo(Cliente c,boolean activo);

}