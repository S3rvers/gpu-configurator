package com.amalitech.gpuconfigurator.service.cloudinary;

import com.amalitech.gpuconfigurator.exception.CloudinaryUploadException;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UploadImageService implements UploadImageServiceImpl {
    private final Cloudinary cloudinary;

    @Override
    public String upload(MultipartFile file) {
        try {
            var response = cloudinary.uploader().upload(file.getBytes(), Map.of());
            return (String) response.get("url");
        } catch (IOException e) {
            throw new CloudinaryUploadException("Error uploading image to Cloudinary", e);
        }
    }

    @Override
    public String uploadCoverImage(MultipartFile coverImage){
        try {
            var response = cloudinary.uploader().upload(coverImage.getBytes(), Map.of());
            return (String) response.get("url");
        } catch (IOException e) {
            throw new CloudinaryUploadException("Error uploading image to Cloudinary", e);
        }
    }
}

