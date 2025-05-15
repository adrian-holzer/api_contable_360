package com.adri.api_contable_360.dto;

import java.time.Year;

public class VencimientoDTO {
    private Integer mes;
    private Integer dia;
    private Integer terminacionCuit;
    private Integer anio;




    public VencimientoDTO() {
        this.anio = Year.now().getValue();
    }
    // Getters y setters

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

    public Integer getTerminacionCuit() {
        return terminacionCuit;
    }

    public void setTerminacionCuit(Integer terminacionCuit) {
        this.terminacionCuit = terminacionCuit;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }
}