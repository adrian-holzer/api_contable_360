package com.adri.api_contable_360.controllers;


import com.adri.api_contable_360.dto.LoginDto;
import com.adri.api_contable_360.dto.RespuestaDto;
import com.adri.api_contable_360.models.Usuario;
import com.adri.api_contable_360.repositories.RolesRepository;
import com.adri.api_contable_360.repositories.UsuarioRepository;
import com.adri.api_contable_360.security.JwtGenerador;
import com.adri.api_contable_360.services.UsuarioService;
import com.adri.api_contable_360.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {



    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private RolesRepository rolesRepository;
    private UsuarioRepository usuariosRepository;
    private JwtGenerador jwtGenerador;


    @Autowired
    private UsuarioService usuarioService;





    @Autowired
    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RolesRepository rolesRepository, UsuarioRepository usuariosRepository,
                           JwtGenerador jwtGenerador) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepository = rolesRepository;
        this.usuariosRepository = usuariosRepository;
        this.jwtGenerador = jwtGenerador;
    }





    //Método para poder logear un usuario y obtener un token
    @PostMapping("login")
    public ResponseEntity<RespuestaDto> login(@RequestBody LoginDto dtoLogin) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dtoLogin.getUsername(), dtoLogin.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerador.generarToken(authentication);
        String username = jwtGenerador.obtenerUsernameDeJwt(token);

        Usuario usuarioLogueado= usuariosRepository.findByNombreUsuario(username).get();


        return new ResponseEntity<>(new RespuestaDto(token,usuarioLogueado), HttpStatus.OK);
    }


    // Obtener usuario logueado

    @GetMapping("/userLogueado")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {


            Usuario usuarioLogueado= usuariosRepository.findByNombreUsuario(authentication.getName()).get();

            return ResponseHandler.generateResponse("Usuario Logueado",HttpStatus.OK,usuarioLogueado);
        }
        return ResponseHandler.generateResponse("No existe usuario logueado",HttpStatus.BAD_REQUEST,null);
    }






}
