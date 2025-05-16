package com.adri.api_contable_360.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Obligacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private String observaciones;
    private boolean activo;

    @OneToMany(mappedBy = "obligacion", cascade = CascadeType.ALL)
    @JsonIgnore // Ignorar esta lista durante la serialización
    private List<Vencimiento> vencimientos;

    // Getters y setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<Vencimiento> getVencimientos() {
        return vencimientos;
    }

    public void setVencimientos(List<Vencimiento> vencimientos) {
        this.vencimientos = vencimientos;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}