package com.adri.api_contable_360.models;

import jakarta.persistence.*;

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

    // Getters y setters


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