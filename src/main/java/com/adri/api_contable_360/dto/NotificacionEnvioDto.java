package com.adri.api_contable_360.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class NotificacionEnvioDto {

    private Long idAsignacionVencimiento;
    private Long idContactoDestinatario;
    private String observacion;
    private List<MultipartFile> archivosAdjuntos;

    // Constructor por defecto
    public NotificacionEnvioDto() {
    }

    // Constructor con todos los campos
    public NotificacionEnvioDto(Long idAsignacionVencimiento, Long idContactoDestinatario, String observacion, List<MultipartFile> archivosAdjuntos) {
        this.idAsignacionVencimiento = idAsignacionVencimiento;
        this.idContactoDestinatario = idContactoDestinatario;
        this.observacion = observacion;
        this.archivosAdjuntos = archivosAdjuntos;
    }

    // Getters y setters
    public Long getIdAsignacionVencimiento() {
        return idAsignacionVencimiento;
    }

    public void setIdAsignacionVencimiento(Long idAsignacionVencimiento) {
        this.idAsignacionVencimiento = idAsignacionVencimiento;
    }

    public Long getIdContactoDestinatario() {
        return idContactoDestinatario;
    }

    public void setIdContactoDestinatario(Long idContactoDestinatario) {
        this.idContactoDestinatario = idContactoDestinatario;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<MultipartFile> getArchivosAdjuntos() {
        return archivosAdjuntos;
    }

    public void setArchivosAdjuntos(List<MultipartFile> archivosAdjuntos) {
        this.archivosAdjuntos = archivosAdjuntos;
    }
}