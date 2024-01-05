package com.amalitech.gpuconfigurator.service.contact;

import com.amalitech.gpuconfigurator.dto.profile.ContactRequest;
import com.amalitech.gpuconfigurator.model.Contact;
import com.amalitech.gpuconfigurator.model.User;

public interface ContactService {
    Contact saveOrUpdate(User user, ContactRequest dto);
}