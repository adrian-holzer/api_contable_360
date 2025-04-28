package com.adri.api_contable_360.dto;

import java.util.List;

public class ObligacionRequestDTO {
    private String nombre;
    private String descripcion;
    private String observacionesObligacion; // Para evitar confusión con las observaciones del vencimiento
    private List<VencimientoDTO> vencimientos;

    // Getters y setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacionesObligacion() {
        return observacionesObligacion;
    }

    public void setObservacionesObligacion(String observacionesObligacion) {
        this.observacionesObligacion = observacionesObligacion;
    }

    public List<VencimientoDTO> getVencimientos() {
        return vencimientos;
    }

    public void setVencimientos(List<VencimientoDTO> vencimientos) {
        this.vencimientos = vencimientos;
    }
}