package com.adri.api_contable_360.repositories;

import com.adri.api_contable_360.models.Cliente;
import com.adri.api_contable_360.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Opción 1: Buscar por el objeto Usuario completo
    List<Cliente> findByUsuarioResponsable(Usuario usuario);




}