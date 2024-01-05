package com.amalitech.gpuconfigurator.service.contact;

import com.amalitech.gpuconfigurator.dto.profile.ContactRequest;
import com.amalitech.gpuconfigurator.model.Contact;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;

    public Contact saveOrUpdate(User user, ContactRequest dto) {
        var userContactOptional = Optional.ofNullable(user.getContact());
        Contact contact = userContactOptional.orElseGet(Contact::new);
        contact.setPhoneNumber(dto.getPhoneNumber());
        contact.setCountry(dto.getCountry());
        contact.setIso2Code(dto.getIso2Code());
        contact.setDialCode(dto.getDialCode());
        return contactRepository.save(contact);
    }
}