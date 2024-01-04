package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.*;
import com.amalitech.gpuconfigurator.dto.otp.ResendOtpDto;
import com.amalitech.gpuconfigurator.service.user.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @CrossOrigin
    @PostMapping("/v1/auth/signup")

    public ResponseEntity<SignupResponse> signup(@Validated @RequestBody SignUpDto request) throws MessagingException, BadRequestException {
        userService.signup(request);

        SignupResponse message = new SignupResponse("Email Verification sent");


        return ResponseEntity.ok(message);
    }

    @CrossOrigin
    @PostMapping("/v1/auth/verify")
    public ResponseEntity<AuthenticationResponse> verify(@RequestBody VerifyUserDto request) {

        AuthenticationResponse user = userService.verifyUserSignup(request);
        return ResponseEntity.ok(user);
    }

    @CrossOrigin
    @PostMapping("/v1/auth/resend-otp")
    public ResponseEntity<GenericResponse> resendOtp(@RequestBody ResendOtpDto resend) throws MessagingException {
        GenericResponse response = userService.resendOtp(resend);
        return ResponseEntity.ok(response);
    }

    @CrossOrigin
    @PostMapping("/v1/auth/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDto request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @CrossOrigin
    @PostMapping("/v1/auth/reset-password")
    public ResponseEntity<ResetResponseDTO> resetPassword(
            @Validated @RequestBody ResetPasswordDTO resetPasswordDto
    ) throws MessagingException {
        userService.resetPassword(resetPasswordDto);
        ResetResponseDTO message = new ResetResponseDTO("Email with otp sent");
        return ResponseEntity.ok(message);
    }

    @CrossOrigin
    @PostMapping("/v1/auth/verify-otp")
    public ResponseEntity<VerifyOTPResponse> verifyOtp(
            @Validated @RequestBody VerifyOtpDTO verifyOtpDTO
    ) {
        userService.verifyResetOtp(verifyOtpDTO);
        VerifyOTPResponse message = new VerifyOTPResponse("OTP verified successfully");
        return ResponseEntity.ok(message);
    }

    @CrossOrigin
    @PostMapping("/v1/auth/change-password")
    public ResponseEntity<GenericResponse> changePassword(
            @Validated @RequestBody ChangePasswordDTO changePasswordDTO

    ) throws BadRequestException {
        GenericResponse changePasswordResponse = userService.changePassword(changePasswordDTO);
        return ResponseEntity.ok(changePasswordResponse);
    }
}
