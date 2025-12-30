package com.chatop.back.rental.service;

import java.util.UUID;
import java.util.Base64;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RentalPictureService {

    @Value("${app.uploads.path}")
    private String uploadsPath;

    public RentalPictureService() {}

    public String encodePicture(String picture) {

        String encodedPicture = null;

        try {
            Path file = Paths.get(uploadsPath + picture);
            if (Files.exists(file)) {
                byte[] bytes = Files.readAllBytes(file);
                String mimeType = Files.probeContentType(file);
                encodedPicture = "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(bytes);
            }
        } catch (Exception ignored) {}

        return encodedPicture;
    }

    public String savePicture(MultipartFile file) {
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadsPath + filename);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            return filename;
        } catch (Exception e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }
}
