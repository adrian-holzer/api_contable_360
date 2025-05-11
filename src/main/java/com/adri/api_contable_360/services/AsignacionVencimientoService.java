package com.adri.api_contable_360.services;

import com.adri.api_contable_360.models.AsignacionVencimiento;
import com.adri.api_contable_360.models.Contacto;
import com.adri.api_contable_360.models.EstadoAsignacion;
import com.adri.api_contable_360.models.Usuario;
import com.adri.api_contable_360.repositories.AsignacionVencimientoRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class AsignacionVencimientoService {

    @Autowired
    private AsignacionVencimientoRepository asignacionVencimientoRepository;

    @Autowired
    private UsuarioService usuarioService; // Necesitamos el servicio de Usuario


    @Autowired
    private ContactoService contactoService;

    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;


    public Optional<AsignacionVencimiento> findById(Long id) {
        return asignacionVencimientoRepository.findById(id);
    }


    public Optional<AsignacionVencimiento> modificarAsignacionVencimiento(
            Long idAsignacionVencimiento,
            String nuevoEstado,
            String observacion) {

        Optional<AsignacionVencimiento> asignacionVencimientoOptional = asignacionVencimientoRepository.findById(idAsignacionVencimiento);

        if (asignacionVencimientoOptional.isPresent()) {
            AsignacionVencimiento asignacionVencimiento = asignacionVencimientoOptional.get();

            // Actualizar el estado si se proporciona
            if (nuevoEstado != null && !nuevoEstado.isEmpty()) {
                try {
                    EstadoAsignacion estado = EstadoAsignacion.valueOf(nuevoEstado.toUpperCase());
                    asignacionVencimiento.setEstado(estado);
                    // Si el estado se cambia a FINALIZADO, establecer la fecha actual
                    if (estado == EstadoAsignacion.FINALIZADO) {
                        asignacionVencimiento.setFechaFinalizacion(LocalDate.now());
                    } else if (nuevoEstado.toUpperCase().equals("PENDIENTE")) {
                        // Si vuelve a pendiente, podrías limpiar la fecha de finalización (opcional)
                        asignacionVencimiento.setFechaFinalizacion(null);
                    }
                } catch (IllegalArgumentException e) {
                    // Manejar estado inválido
                    return Optional.empty();
                }
            }

            // Actualizar la observación si se proporciona
            if (observacion != null) {
                asignacionVencimiento.setObservacion(observacion);
            }

            return Optional.of(asignacionVencimientoRepository.save(asignacionVencimiento));
        }

        return Optional.empty();
    }



    public boolean crearYEnviarNotificacion(Long idAsignacionVencimiento, Long idContactoDestinatario, String observacion, List <MultipartFile> archivosAdjuntos) {
        Optional<AsignacionVencimiento> asignacionVencimientoOptional = asignacionVencimientoRepository.findById(idAsignacionVencimiento);
        Optional<Contacto> contactoOptional = contactoService.getContactoById(idContactoDestinatario);

        if (asignacionVencimientoOptional.isEmpty()) {
            throw new IllegalArgumentException("Asignación de vencimiento no encontrada con ID: " + idAsignacionVencimiento);
        }

        if (contactoOptional.isEmpty()) {
            throw new IllegalArgumentException("Contacto no encontrado con ID: " + idContactoDestinatario);
        }

        AsignacionVencimiento asignacionVencimiento = asignacionVencimientoOptional.get();
        Contacto contactoDestinatario = contactoOptional.get();
        String emailDestinatario = contactoDestinatario.getCorreo();

//        // Obtener el usuario autenticado
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//        // Verificar que el usuario tiene el rol de "PROFESIONAL" o "ADMIN"
//        if (!userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("PROFESIONAL") || a.getAuthority().equals("ADMIN"))) {
//            throw new SecurityException("Usuario no autorizado para enviar notificaciones");
//        }

        // Crear el contenido del correo
        String asunto = "Notificación de cumplimiento de la Obligación " + asignacionVencimiento.getAsignacion().getObligacion().getNombre();
        String cuerpo = observacion != null ? observacion : "";

        // Enviar la notificación por email con archivos adjuntos opcionales
        try {
            emailService.enviarEmailConAdjuntos(
                    host, port, username, password,
                    emailDestinatario, asunto, cuerpo, archivosAdjuntos
            );
            return true;
        } catch (MessagingException e) {
            // Manejar la excepción de envío de correo
            e.printStackTrace();
            return false;
        }
    }

}