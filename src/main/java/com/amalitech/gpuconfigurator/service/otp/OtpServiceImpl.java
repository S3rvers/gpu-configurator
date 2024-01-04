package com.amalitech.gpuconfigurator.service.otp;

import com.amalitech.gpuconfigurator.model.Otp;
import com.amalitech.gpuconfigurator.model.OtpType;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private static final int OTP_LENGTH = 6;
    private final OtpRepository otpRepository;

    @Override
    public String generateOtp() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(secureRandom.nextInt(10));
        }

        return otp.toString();
    }

    @Override
    public Boolean isValidOtp(String email, String otpCode, OtpType otpType) {
        Otp otp = otpRepository.findByEmailAndCodeAndType(email, otpCode, otpType).orElseThrow(() -> new UsernameNotFoundException("unable to verify"));

        Boolean isExpiredOtp = isExpiredOtp(otp);
        return !isExpiredOtp;
    }


    @Override
    public Boolean isExpiredOtp(Otp otpInstance) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = otpInstance.getExpiration();

        return currentTime.isAfter(expirationTime);
    }

    @Override
    public String generateAndSaveOtp(User user, OtpType type) {
        String code = this.generateOtp();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);

        var otpInstance = Otp
                .builder()
                .code(code)
                .email(user.getEmail())
                .expiration(expirationTime)
                .type(type)
                .build();

        otpRepository.save(otpInstance);

        return code;
    }

    @Override
    public void deleteOtp(String email, String otpCode) {
        otpRepository.deleteByEmailAndCode(email, otpCode);
    }

}
