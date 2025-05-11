package com.adri.api_contable_360.repositories;

import com.adri.api_contable_360.models.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {
    // Spring Data JPA proporciona los métodos básicos como save(), findById(), findAll(), deleteById(), etc.
    // Puedes agregar métodos de consulta personalizados si los necesitas
}