package com.adri.api_contable_360.controllers;

import com.adri.api_contable_360.dto.ClienteDto;
import com.adri.api_contable_360.models.Cliente;
import com.adri.api_contable_360.models.Usuario;
import com.adri.api_contable_360.repositories.ClienteRepository;
import com.adri.api_contable_360.services.ClienteService;
import com.adri.api_contable_360.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes() {
        List<Cliente> clientes = clienteService.getAllClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<?> getClienteById(@PathVariable Long idCliente) {
        Cliente cliente = clienteService.findById(idCliente);

        if (cliente != null) {
            return new ResponseEntity<>(cliente, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cliente no encontrado con ID: " + idCliente, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody ClienteDto clienteDto) {

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setCuit(clienteDto.getCuit());
        nuevoCliente.setActividadAfip(clienteDto.getActividadAfip());
        nuevoCliente.setNombre(clienteDto.getNombre());
        nuevoCliente.setDomicilioFiscal(clienteDto.getDomicilioFiscal());
        nuevoCliente.setDomicilioLegal(clienteDto.getDomicilioLegal());



        Cliente savedCliente = clienteService.saveCliente(nuevoCliente);
        return new ResponseEntity<>(savedCliente, HttpStatus.CREATED);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
//        Optional<Cliente> existingCliente = clienteService.getClienteById(id);
//        if (existingCliente.isPresent()) {
//            cliente.setIdCliente(id);
//            Cliente updatedCliente = clienteService.saveCliente(cliente);
//            return new ResponseEntity<>(updatedCliente, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCliente(@PathVariable Long id) {
        clienteService.deleteCliente(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    @PostMapping("/asignar/{idCliente}/usuario/{idUsuario}")
    public ResponseEntity<String> asignarClienteAUsuario(@PathVariable Long idCliente, @PathVariable Long idUsuario) {
        return clienteService.asignarClienteAUsuario(idCliente, idUsuario);
    }


    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Cliente>> obtenerClientesPorUsuario( @PathVariable Long idUsuario) {
        Optional<Usuario> usuarioOptional = usuarioService.findById(idUsuario);
        if (usuarioOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Cliente> clientes = clienteRepository.findByUsuarioResponsable(usuarioOptional.get());
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }
}