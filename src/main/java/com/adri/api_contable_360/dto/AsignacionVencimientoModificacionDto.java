package com.adri.api_contable_360.dto;

import java.time.LocalDate;

public class AsignacionVencimientoModificacionDto {

    private String estado;
    private String observacion;

    // Constructor por defecto
    public AsignacionVencimientoModificacionDto() {
    }

    // Constructor con los campos que se pueden modificar
    public AsignacionVencimientoModificacionDto(String estado, String observacion) {
        this.estado = estado;
        this.observacion = observacion;
    }

    // Getters y setters
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}