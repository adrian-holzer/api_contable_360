package com.adri.api_contable_360.services;


import com.adri.api_contable_360.models.Cliente;
import com.adri.api_contable_360.models.Usuario;
import com.adri.api_contable_360.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioService usuarioService;

    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    public Cliente findById(Long id) {

        return clienteRepository.findById(id).orElse(null);
    }

    public Cliente saveCliente(Cliente cliente) {


// Extraer la terminación del CUIT y setear el campo
        String cuit = cliente.getCuit();
        if (cuit != null && cuit.length() > 0) {
            try {
                String ultimoDigito = cuit.substring(cuit.length() - 1);
                cliente.setTerminacionCuit(Integer.parseInt(ultimoDigito));
            } catch (NumberFormatException e) {
                // Manejar el caso en que el último carácter no sea un número
                cliente.setTerminacionCuit(null); // O algún valor por defecto
                System.err.println("Error al extraer la terminación del CUIT: " + e.getMessage());
            }
        } else {
            cliente.setTerminacionCuit(null); // Si el CUIT es nulo o vacío
        }
        return clienteRepository.save(cliente);
    }



    public ResponseEntity<String> asignarClienteAUsuario(Long idCliente, Long idUsuario) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(idCliente);
        Optional<Usuario> usuarioOptional = usuarioService.findById(idUsuario);

        if (clienteOptional.isEmpty()) {
            return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
        }

        if (usuarioOptional.isEmpty()) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        Cliente cliente = clienteOptional.get();


            cliente.setUsuarioResponsable(usuarioOptional.get());
            clienteRepository.save(cliente);
            return new ResponseEntity<>("Cliente asignado al usuario exitosamente", HttpStatus.OK);

    }





    public void deleteCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}