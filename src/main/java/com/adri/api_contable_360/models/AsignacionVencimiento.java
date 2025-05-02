package com.adri.api_contable_360.models;

import jakarta.persistence.*;

@Entity
public class AsignacionVencimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAsignacionVencimiento;

    @ManyToOne
    @JoinColumn(name = "idAsignacion")
    private Asignacion asignacion;

    @ManyToOne
    @JoinColumn(name = "idVencimiento")
    private Vencimiento vencimiento;

    @Enumerated(EnumType.STRING)
    private EstadoAsignacion estado = EstadoAsignacion.PENDIENTE;

    private String observacion;

    // Getters y setters

    public Long getIdAsignacionVencimiento() {
        return idAsignacionVencimiento;
    }

    public void setIdAsignacionVencimiento(Long idAsignacionVencimiento) {
        this.idAsignacionVencimiento = idAsignacionVencimiento;
    }

    public Asignacion getAsignacion() {
        return asignacion;
    }

    public void setAsignacion(Asignacion asignacion) {
        this.asignacion = asignacion;
    }

    public Vencimiento getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(Vencimiento vencimiento) {
        this.vencimiento = vencimiento;
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