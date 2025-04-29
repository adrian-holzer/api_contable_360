package com.adri.api_contable_360.repositories;

import com.adri.api_contable_360.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Métodos adicionales si necesitas consultas personalizadas
}