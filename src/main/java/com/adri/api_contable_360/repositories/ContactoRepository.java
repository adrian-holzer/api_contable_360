package com.adri.api_contable_360.repositories;

import com.adri.api_contable_360.models.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {
    // Métodos adicionales si necesitas consultas personalizadas
}