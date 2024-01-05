package com.amalitech.gpuconfigurator.service.profile;

import com.amalitech.gpuconfigurator.dto.profile.BasicInformationRequest;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.auth.UserPasswordRequest;
import com.amalitech.gpuconfigurator.model.User;
import org.apache.coyote.BadRequestException;

import java.security.Principal;

public interface ProfileService {
    GenericResponse updateBasicInformation(BasicInformationRequest dto, Principal principal);

    User getUserProfile(Principal principal);

    GenericResponse updateUserPassword(UserPasswordRequest dto, Principal principal) throws BadRequestException;
}