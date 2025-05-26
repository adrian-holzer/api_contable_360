package com.adri.api_contable_360;


import com.adri.api_contable_360.models.Roles;
import com.adri.api_contable_360.models.Usuario;
import com.adri.api_contable_360.repositories.RolesRepository;
import com.adri.api_contable_360.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private final RolesRepository roleRepository;
    @Autowired
    private final UsuarioRepository usuariosRepository;
    private PasswordEncoder passwordEncoder;



    public DataInitializer(RolesRepository roleRepository, UsuarioRepository usuariosRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;
    }


    //crear roles ADMIN

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Roles adminRole = new Roles();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }




// Crear usuario admin

        Usuario usuario = new Usuario();

        usuario.setCuit("20-30987654-3");
        usuario.setNombreUsuario("demo_admin");
        usuario.setNombreApellido("Demo Admin");
        usuario.setCorreo("demo_admin@email.com'");
        usuario.setContrasena(passwordEncoder.encode("adminpass"));

        // Obtener los roles del repositorio
        Roles adminRole = roleRepository.findByName("ADMIN").orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));
        // Roles profesionalRole = roleRepository.findByName("PROFESIONAL").orElseThrow(() -> new RuntimeException("Rol PROFESIONAL no encontrado"));

        // Asignar ambos roles al usuario
        usuario.setRoles(Arrays.asList(adminRole));


        usuariosRepository.save(usuario);

    }
}