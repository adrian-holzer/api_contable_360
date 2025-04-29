package com.adri.api_contable_360.repositories;

import com.adri.api_contable_360.models.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    // Métodos adicionales si necesitas consultas personalizadas
}