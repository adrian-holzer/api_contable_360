package com.adri.api_contable_360.controllers;

import com.adri.api_contable_360.models.Contacto;
import com.adri.api_contable_360.services.ContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contactos")
public class ContactoController {

    @Autowired
    private ContactoService contactoService;

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

    @PostMapping
    public ResponseEntity<Contacto> createContacto(@RequestBody Contacto contacto) {
        Contacto savedContacto = contactoService.saveContacto(contacto);
        return new ResponseEntity<>(savedContacto, HttpStatus.CREATED);
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