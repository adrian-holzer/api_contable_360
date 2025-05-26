package com.adri.api_contable_360.dto;

import com.adri.api_contable_360.models.Usuario;
import lombok.Data;

//Esta clase va a ser la que nos devolverá la información con el token y el tipo que tenga este
@Data
public class RespuestaDto {
    private String accessToken;
    private String tokenType = "Bearer ";
    private Usuario usuarioLogueado;

    public RespuestaDto(String accessToken, Usuario usuarioLogueado) {
        this.accessToken = accessToken;
        this.usuarioLogueado=usuarioLogueado;
    }
}
