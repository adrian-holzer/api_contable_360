package com.adri.api_contable_360.controllers;

import com.adri.api_contable_360.models.Cliente;
import com.adri.api_contable_360.models.Contacto;
import com.adri.api_contable_360.services.ClienteService;
import com.adri.api_contable_360.services.ContactoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contactos")
public class ContactoController {

    @Autowired
    private ContactoService contactoService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Contacto>> getAllContactos() {
        List<Contacto> contactos = contactoService.getAllContactos();
        return new ResponseEntity<>(contactos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contacto> getContactoById(@PathVariable Long id) {
        Optional<Contacto> contacto = contactoService.getContactoById(id);
        return contacto.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/cliente/{idCliente}") // Más específico y RESTful
    public ResponseEntity<?> getContactosByCliente(@PathVariable Long idCliente) {
        Cliente cliente = clienteService.findById(idCliente);

        if (cliente == null) {
            return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
        }

        List<Contacto> contactos = cliente.getContactos(); // Obtener la lista directamente
        return new ResponseEntity<>(contactos, HttpStatus.OK);
    }
    @PostMapping("/clientes/{idCliente}")
    public ResponseEntity<?> crearContacto(@PathVariable Long idCliente, @Valid @RequestBody Contacto contacto, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        Cliente cliente = clienteService.findById(idCliente);

        if (cliente==null) {
            return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
        }

        contacto.setCliente(cliente);
        Contacto nuevoContacto = contactoService.saveContacto(contacto);
        return new ResponseEntity<>(nuevoContacto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contacto> updateContacto(@PathVariable Long id, @RequestBody Contacto contacto) {
        Optional<Contacto> existingContacto = contactoService.getContactoById(id);
        if (existingContacto.isPresent()) {
            contacto.setIdContacto(id);
            Contacto updatedContacto = contactoService.saveContacto(contacto);
            return new ResponseEntity<>(updatedContacto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteContacto(@PathVariable Long id) {
        contactoService.deleteContacto(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}