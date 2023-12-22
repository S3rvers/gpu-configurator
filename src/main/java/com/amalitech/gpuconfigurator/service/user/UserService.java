package com.amalitech.gpuconfigurator.service.user;

import com.amalitech.gpuconfigurator.dto.*;
import com.amalitech.gpuconfigurator.dto.auth.AuthenticationResponse;
import com.amalitech.gpuconfigurator.dto.auth.LoginDto;
import com.amalitech.gpuconfigurator.dto.auth.ResetPasswordDTO;
import com.amalitech.gpuconfigurator.dto.auth.SignUpDto;
import com.amalitech.gpuconfigurator.dto.otp.ResendOtpDto;
import com.amalitech.gpuconfigurator.dto.otp.VerifyOtpDTO;
import com.amalitech.gpuconfigurator.dto.otp.VerifyUserDto;
import com.amalitech.gpuconfigurator.dto.profile.ChangePasswordDTO;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;

public interface UserService {
    void signup(SignUpDto request) throws MessagingException, BadRequestException;

    AuthenticationResponse login(LoginDto request);

    AuthenticationResponse verifyUserSignup(VerifyUserDto request);

    GenericResponse changePassword(ChangePasswordDTO changePasswordDTO) throws BadRequestException;

    void verifyResetOtp(VerifyOtpDTO verifyOtpDTO);

    void resetPassword(ResetPasswordDTO resetPasswordDto) throws MessagingException;

    GenericResponse resendOtp(ResendOtpDto resend) throws MessagingException;
}
