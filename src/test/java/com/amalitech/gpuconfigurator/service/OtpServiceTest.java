package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.model.Otp;
import com.amalitech.gpuconfigurator.model.OtpType;
import com.amalitech.gpuconfigurator.model.Role;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.OtpRepository;
import com.amalitech.gpuconfigurator.service.otp.OtpServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OtpServiceTest {

    @InjectMocks
    private OtpServiceImpl otpService;

    @Mock
    private OtpRepository otpRepository;

    private User user;
    private Otp otpInstance;

    @BeforeEach
    void setUp() {

        String userId = "550e8400-e29b-41d4-a716-446655440000";
        user = User.builder()
                .id(UUID.fromString(userId))
                .firstName("dickson")
                .lastName("anyaele")
                .email("kaycydickson@gmail.com")
                .password("dissonance")
                .isVerified(false)
                .role(Role.USER).build();

        otpInstance = Otp.builder()
                .expiration(LocalDateTime.now().plusMinutes(-5))
                .code("1234")
                .email("kaycydicksons@gmail.com")
                .type(OtpType.CREATE)
                .build();

    }


    @Test
    public void generateOtp_shouldGenerateNonEmptyString() {
        String otp = otpService.generateOtp();

        assertNotNull(otp);
        assertFalse(otp.isEmpty());
    }


    @Test
    public void generateAndSaveOtp_shouldGenerateAndSaveOtp() {
        String code = otpService.generateAndSaveOtp(user, OtpType.CREATE);

        assertNotNull(code);
        assertFalse(code.isEmpty());
    }

    @Test
    public void isExpiredOtp_shouldPassIfOtpIsNotExpired() {
        var otpInstance = Otp.builder()
                .expiration(LocalDateTime.now().plusMinutes(5))
                .code("1234")
                .email("kaycydicksons@gmail.com")
                .type(OtpType.CREATE)
                .build();

        Boolean isNotExpired = otpService.isExpiredOtp(otpInstance);

        assertFalse(isNotExpired);
    }

    @Test
    public void isExpiredOtp_shouldPassIfOtpIsExpired() {


        Boolean isExpired = otpService.isExpiredOtp(otpInstance);

        assertTrue(isExpired);
    }


}
