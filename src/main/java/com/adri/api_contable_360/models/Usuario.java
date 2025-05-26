package com.adri.api_contable_360.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(unique = true) // Asegura que el valor de 'cuit' sea único en la base de datos
    private String cuit;

    private String nombreUsuario;
    private String nombreApellido;

    @JsonIgnore
    private String contrasena;
    private String correo;

    @OneToMany(mappedBy = "usuarioResponsable")
    @JsonIgnore
    private List<Cliente> clientesResponsables;

    @OneToMany(mappedBy = "usuarioFinalizo")
    @JsonIgnore
    private List<AsignacionVencimiento> asignacionesVencimientosFinalizadas;




    //Usamos fetchType en EAGER para que cada vez que se acceda o se extraiga un usuario de la BD, este se traiga todos sus roles
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    /*Con JoinTable estaremos creando una tabla que unirá la tabla de usuario y role, con lo cual tendremos un total de 3 tablas
    relacionadas en la tabla "usuarios_roles", a través de sus columnas usuario_id que apuntara al ID de la tabla usuario
    y role_id que apuntara al Id de la tabla role */
    @JoinTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "idUsuario")
            ,inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id_role"))
    private List<Roles> roles = new ArrayList<>();



    // Constructor por defecto (necesario para JPA)
    public Usuario() {
    }

    // Constructor con todos los campos
    public Usuario(String cuit, String nombreUsuario, String nombreApellido, String contrasena, String correo) {
        this.cuit = cuit;
        this.nombreUsuario = nombreUsuario;
        this.nombreApellido = nombreApellido;
        this.contrasena = contrasena;
        this.correo = correo;
    }

    // Getters y setters para todos los campos
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreApellido() {
        return nombreApellido;
    }

    public void setNombreApellido(String nombreApellido) {
        this.nombreApellido = nombreApellido;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public List<Cliente> getClientesResponsables() {
        return clientesResponsables;
    }

    public void setClientesResponsables(List<Cliente> clientesResponsables) {
        this.clientesResponsables = clientesResponsables;
    }

    public List<AsignacionVencimiento> getAsignacionesVencimientosFinalizadas() {
        return asignacionesVencimientosFinalizadas;
    }

    public void setAsignacionesVencimientosFinalizadas(List<AsignacionVencimiento> asignacionesVencimientosFinalizadas) {
        this.asignacionesVencimientosFinalizadas = asignacionesVencimientosFinalizadas;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }
}