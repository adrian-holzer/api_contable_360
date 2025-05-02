package com.adri.api_contable_360.dto;

import com.adri.api_contable_360.models.EstadoAsignacion;

import java.util.List;

// Clase para recibir los datos necesarios para crear una asignación
public class AsignacionDto {
    private Long idCliente;
    private List<Long> idsObligaciones; // Cambiamos a una lista de IDs
    private EstadoAsignacion estado = EstadoAsignacion.PENDIENTE;
    private String observacion;

    // Getters y setters

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public List<Long> getIdsObligaciones() {
        return idsObligaciones;
    }

    public void setIdsObligaciones(List<Long> idsObligaciones) {
        this.idsObligaciones = idsObligaciones;
    }

    public EstadoAsignacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsignacion estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
