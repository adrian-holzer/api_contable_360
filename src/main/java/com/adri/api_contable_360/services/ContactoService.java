package com.adri.api_contable_360.services;

import com.adri.api_contable_360.models.Contacto;
import com.adri.api_contable_360.repositories.ContactoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactoService {

    @Autowired
    private ContactoRepository contactoRepository;

    public List<Contacto> getAllContactos() {
        return contactoRepository.findAll();
    }

    public Optional<Contacto> getContactoById(Long id) {
        return contactoRepository.findById(id);
    }

    public Contacto saveContacto(Contacto contacto) {
        return contactoRepository.save(contacto);
    }

    public void deleteContacto(Long id) {
        contactoRepository.deleteById(id);
    }
}