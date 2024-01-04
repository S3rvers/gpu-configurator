package com.amalitech.gpuconfigurator.service.user;

import com.amalitech.gpuconfigurator.dto.*;
import com.amalitech.gpuconfigurator.dto.otp.ResendOtpDto;
import com.amalitech.gpuconfigurator.model.OtpType;
import com.amalitech.gpuconfigurator.model.Role;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.service.email.EmailServiceImpl;
import com.amalitech.gpuconfigurator.service.jwt.JwtServiceImpl;
import com.amalitech.gpuconfigurator.service.otp.OtpServiceImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final JwtServiceImpl jwtServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final OtpServiceImpl otpService;
    private final EmailServiceImpl emailService;

    @Override
    public void signup(SignUpDto request) throws MessagingException, BadRequestException {

      Optional<User> newUser = repository.findByEmail(request.getEmail());
      if(newUser.isPresent()){
          throw new BadRequestException("Email Already Exist");
      }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .isVerified(false)
                .build();



        repository.save(user);
        String code = otpService.generateAndSaveOtp(user, OtpType.CREATE);
        emailService.sendOtpMessage(user.getEmail(), code, OtpType.CREATE);
    }

    @Override
    public AuthenticationResponse login(LoginDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = repository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Email or Password incorrect"));
        if (!user.getIsVerified()) {
            throw new UsernameNotFoundException("email not verified");
        }
        var jwtToken = jwtServiceImpl.generateToken(user);

        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .token(jwtToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }


    @Override
    public AuthenticationResponse verifyUserSignup(VerifyUserDto request) {
        boolean isValidOtp = otpService.isValidOtp(request.email(), request.code(), OtpType.CREATE);
        if (!isValidOtp) {
            throw new UsernameNotFoundException("could not verify otp");
        }

        User user = repository.findByEmail(request.email()).orElseThrow(() -> new UsernameNotFoundException("user does not exist"));
        user.setIsVerified(true);

        repository.save(user);

        var jwtToken = jwtServiceImpl.generateToken(user);

        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .token(jwtToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

    }

    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) throws MessagingException {
        User user = repository.findByEmail(resetPasswordDTO.getEmail()).orElseThrow(() -> new UsernameNotFoundException("we have sent you an email"));

        var otp = otpService.generateAndSaveOtp(user, OtpType.RESET);

        emailService.sendOtpMessage(user.getEmail(), otp, OtpType.RESET);
    }

    @Override
    public void verifyResetOtp(VerifyOtpDTO verifyOtpDTO) {
        boolean isValidOtp = otpService.isValidOtp(verifyOtpDTO.getEmail(), verifyOtpDTO.getOtpCode(), OtpType.RESET);
        if (!isValidOtp) {
            throw new UsernameNotFoundException("could not verify otp");
        }
    }

    @Override
    public GenericResponse changePassword(ChangePasswordDTO changePasswordDTO) throws BadRequestException {
        User user = repository.findByEmail(changePasswordDTO.getEmail()).orElseThrow(() -> new UsernameNotFoundException("cannot change password, try again"));
        Boolean otpValid = otpService.isValidOtp(changePasswordDTO.getEmail(), changePasswordDTO.getOtpCode(), OtpType.RESET);
        if(!otpValid) {
            throw new BadRequestException("could not change password");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.newPassword));
        repository.save(user);
        return new GenericResponse(201, "user changed password successfully");
    }

    @Override
    public GenericResponse resendOtp(ResendOtpDto resend) throws MessagingException {
        User user = repository.findByEmail(resend.email()).orElseThrow(() -> new UsernameNotFoundException("could not send otp as this user does not exist"));
        String otp = otpService.generateAndSaveOtp(user, OtpType.valueOf(resend.type()));
        emailService.sendOtpMessage(user.getEmail(), otp, OtpType.valueOf(resend.type()));

        return new GenericResponse(201, "otp has been sent");
    }


}
