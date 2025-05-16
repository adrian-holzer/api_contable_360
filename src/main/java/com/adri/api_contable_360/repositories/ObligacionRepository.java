package com.adri.api_contable_360.repositories;

import com.adri.api_contable_360.models.Obligacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ObligacionRepository extends JpaRepository<Obligacion, Long> {
    Optional<Obligacion> findByNombre(String nombre);
    List<Obligacion> findByActivo(boolean activo);



}