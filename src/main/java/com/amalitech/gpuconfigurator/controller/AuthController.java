package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.*;
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
    private final UserService userServiceImpl;

    @CrossOrigin
    @PostMapping("/v1/auth/signup")

    public ResponseEntity<SignupResponse> signup(@Validated @RequestBody SignUpDto request) throws MessagingException, BadRequestException {
       userServiceImpl.signup(request);

       SignupResponse message = new SignupResponse("Email Verification sent");


       return ResponseEntity.ok(message);
    }

    @CrossOrigin
    @PostMapping("/v1/auth/verify")
    public ResponseEntity<AuthenticationResponse> verify(@RequestBody VerifyUserDto request)  {

        AuthenticationResponse user = userServiceImpl.verifyUserSignup(request);
        return ResponseEntity.ok(user);
    }

    @CrossOrigin
    @PostMapping("/v1/auth/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDto request) {
        return ResponseEntity.ok(userServiceImpl.login(request));
    }

    @CrossOrigin
    @PostMapping("/v1/auth/reset-password")
    public ResponseEntity<ResetResponseDTO> resetPassword(
            @Validated @RequestBody ResetPasswordDTO resetPasswordDto
    ) throws MessagingException {
        userServiceImpl.resetPassword(resetPasswordDto);
        ResetResponseDTO message = new ResetResponseDTO("Email with otp sent");
        return ResponseEntity.ok(message);
    }

    @CrossOrigin
    @PostMapping("/v1/auth/verify-otp")
    public ResponseEntity<VerifyOTPResponse> verifyOtp(
            @Validated @RequestBody VerifyOtpDTO verifyOtpDTO
    ) {
        userServiceImpl.verifyResetOtp(verifyOtpDTO);
        VerifyOTPResponse message = new VerifyOTPResponse("OTP verified successfully");
        return ResponseEntity.ok(message);
    }
    @CrossOrigin
    @PostMapping("/v1/auth/change-password")
    public ResponseEntity<String> changePassword(
            @Validated @RequestBody ChangePasswordDTO changePasswordDTO

    ) {
        return ResponseEntity.ok(userServiceImpl.changePassword(changePasswordDTO));
    }

}
