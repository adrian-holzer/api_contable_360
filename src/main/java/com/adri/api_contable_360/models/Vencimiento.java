package com.adri.api_contable_360.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Vencimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "obligacion_id")
    private Obligacion obligacion;

    private Integer terminacionCuit;
    private Integer mes;
    private Integer dia;
    private String observaciones;
    private LocalDate fechaVencimiento; // Nuevo campo para la fecha de vencimiento

    // Getters y setters

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTerminacionCuit() {
        return terminacionCuit;
    }

    public void setTerminacionCuit(Integer terminacionCuit) {
        this.terminacionCuit = terminacionCuit;
    }

    public Obligacion getObligacion() {
        return obligacion;
    }

    public void setObligacion(Obligacion obligacion) {
        this.obligacion = obligacion;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}