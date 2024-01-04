package com.amalitech.gpuconfigurator.service.user;

import com.amalitech.gpuconfigurator.dto.*;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;

public interface UserService {
    void signup(SignUpDto request) throws MessagingException, BadRequestException;

    AuthenticationResponse login(LoginDto request);

    AuthenticationResponse verifyUserSignup(VerifyUserDto request);

    String changePassword(ChangePasswordDTO changePasswordDTO);

    void verifyResetOtp(VerifyOtpDTO verifyOtpDTO);

    void resetPassword(ResetPasswordDTO resetPasswordDto) throws MessagingException;
}
