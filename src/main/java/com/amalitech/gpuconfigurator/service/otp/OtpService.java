package com.amalitech.gpuconfigurator.service.otp;

import com.amalitech.gpuconfigurator.model.Otp;
import com.amalitech.gpuconfigurator.model.OtpType;
import com.amalitech.gpuconfigurator.model.User;

public interface OtpService {

    String generateOtp();

    Boolean isValidOtp(String email, String otpCode, OtpType type);

    Boolean isExpiredOtp(Otp otp);

    String generateAndSaveOtp(User user, OtpType type);

    void deleteOtp(String email, String otpCode);
}
