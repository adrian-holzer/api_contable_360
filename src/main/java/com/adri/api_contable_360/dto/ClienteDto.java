package com.adri.api_contable_360.dto;


public class ClienteDto {
    private Long idCliente;
    private String cuit;
    private String actividadAfip;
    private String domicilioLegal;
    private String domicilioFiscal;
    private String nombre;
    private Integer terminacionCuit; // Nuevo campo en el DTO

    public ClienteDto() {
    }

    public ClienteDto(Long idCliente, String cuit, String actividadAfip, String domicilioLegal, String domicilioFiscal, String nombre, Integer terminacionCuit) {
        this.idCliente = idCliente;
        this.cuit = cuit;
        this.actividadAfip = actividadAfip;
        this.domicilioLegal = domicilioLegal;
        this.domicilioFiscal = domicilioFiscal;
        this.nombre = nombre;
        this.terminacionCuit = terminacionCuit;
    }

    // Getters y setters (incluyendo para terminacionCuit)
    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getActividadAfip() {
        return actividadAfip;
    }

    public void setActividadAfip(String actividadAfip) {
        this.actividadAfip = actividadAfip;
    }

    public String getDomicilioLegal() {
        return domicilioLegal;
    }

    public void setDomicilioLegal(String domicilioLegal) {
        this.domicilioLegal = domicilioLegal;
    }

    public String getDomicilioFiscal() {
        return domicilioFiscal;
    }

    public void setDomicilioFiscal(String domicilioFiscal) {
        this.domicilioFiscal = domicilioFiscal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getTerminacionCuit() {
        return terminacionCuit;
    }

    public void setTerminacionCuit(Integer terminacionCuit) {
        this.terminacionCuit = terminacionCuit;
    }
}