package com.chatop.back.rental.service.picture;

import java.util.Base64;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class EncodeRentalPictureService {

    @Value("${app.uploads.path}")
    private String uploadsPath;

    public String execute(String picture) {

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
}
