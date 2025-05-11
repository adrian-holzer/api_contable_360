package com.adri.api_contable_360.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAsignacion;

    private String observacion;

    @ManyToOne
    @JoinColumn(name = "idCliente")// Ignoramos la serialización de la relación cliente para evitar bucles y redundancia
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "idObligacion")
    private Obligacion obligacion; // Agregamos la relación con Obligacion

    private boolean activo; // Agregamos la relación con Obligacion


    @OneToMany(mappedBy = "asignacion", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AsignacionVencimiento> asignacionesVencimientos;

    public Long getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(Long idAsignacion) {
        this.idAsignacion = idAsignacion;
    }



    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Obligacion getObligacion() {
        return obligacion;
    }

    public void setObligacion(Obligacion obligacion) {
        this.obligacion = obligacion;
    }
    public List<AsignacionVencimiento> getAsignacionesVencimientos() {
        return asignacionesVencimientos;
    }

    public void setAsignacionesVencimientos(List<AsignacionVencimiento> asignacionesVencimientos) {
        this.asignacionesVencimientos = asignacionesVencimientos;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}