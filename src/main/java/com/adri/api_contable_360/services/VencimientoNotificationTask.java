package com.adri.api_contable_360.services;

import com.adri.api_contable_360.models.AsignacionVencimiento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VencimientoNotificationTask {

    private static final Logger logger = LoggerFactory.getLogger(VencimientoNotificationTask.class);

    @Autowired
    private AsignacionVencimientoService asignacionVencimientoService;

//    // Se ejecutará cada 24 horas (86400000 milisegundos)
//    @Scheduled(fixedRate = 86400000)
//    public void verificarAsignacionesProximasAVencer() {
//        logger.info("Verificando asignaciones próximas a vencer...");
//        List<AsignacionVencimiento> proximasAVencer = asignacionVencimientoService.getAsignacionesProximasAVencer();
//        if (!proximasAVencer.isEmpty()) {
//            logger.warn("¡Atención! Se encontraron asignaciones próximas a vencer:");
//            for (AsignacionVencimiento av : proximasAVencer) {
//                logger.warn("- ID: {}, Obligación: {}, Vencimiento: {}", av.getIdAsignacionVencimiento(), av.getAsignacion().getObligacion().getNombre(), av.getVencimiento().getFechaVencimiento());
//                // Aquí podrías implementar la lógica para enviar la notificación (por ejemplo, por correo electrónico, a través de un WebSocket, etc.)
//
//            }
//        } else {
//            logger.info("No hay asignaciones próximas a vencer en los próximos 10 días.");
//        }
//    }
}