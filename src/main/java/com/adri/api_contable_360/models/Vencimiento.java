package com.adri.api_contable_360.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

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
    private Integer anio;
    private String observaciones;
    private LocalDate fechaVencimiento; // Nuevo campo para la fecha de vencimiento

    @OneToMany(mappedBy = "vencimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Ignora esta propiedad al serializar a JSON
    private List<AsignacionVencimiento> asignacionesVencimientos;

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

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public List<AsignacionVencimiento> getAsignacionesVencimientos() {
        return asignacionesVencimientos;
    }

    public void setAsignacionesVencimientos(List<AsignacionVencimiento> asignacionesVencimientos) {
        this.asignacionesVencimientos = asignacionesVencimientos;
    }
}