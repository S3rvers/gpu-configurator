package com.amalitech.gpuconfigurator.service.profile;

import com.amalitech.gpuconfigurator.dto.profile.BasicInformationRequest;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.auth.UserPasswordRequest;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.contact.ContactService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ContactService contactService;

    @Override
    public GenericResponse updateBasicInformation(BasicInformationRequest dto, Principal principal) {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setContact(contactService.saveOrUpdate(user, dto.getContact()));
        userRepository.save(user);
        return new GenericResponse(200, "Basic information updated successfully");
    }

    public User getUserProfile(Principal principal) throws UsernameNotFoundException {
        return (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
    }

    @Override
    public GenericResponse updateUserPassword(UserPasswordRequest dto, Principal principal) throws BadRequestException {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new BadRequestException("Password and password confirmation are not equal");
        }
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("invalid password");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        return new GenericResponse(201, "password updated successfully");
    }
}