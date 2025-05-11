package com.adri.api_contable_360.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContacto;

    @NotBlank(message = "El nombre del contacto es obligatorio")
    private String nombre;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe tener un formato válido")
    private String correo;

    @Pattern(regexp = "^\\d+$", message = "El número de teléfono debe contener solo dígitos")
    private String numTelefono;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    // Constructor por defecto
    public Contacto() {
    }

    // Constructor con campos obligatorios
    public Contacto(String nombre, String correo, String numTelefono, Cliente cliente) {
        this.nombre = nombre;
        this.correo = correo;
        this.numTelefono = numTelefono;
        this.cliente = cliente;
    }

    // Getters y setters
    public Long getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Long idContacto) {
        this.idContacto = idContacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNumTelefono() {
        return numTelefono;
    }

    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}