package com.chatop.back.rental.service.picture;

import java.util.UUID;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

@Service
public class SaveRentalPictureService {

    @Value("${app.uploads.path}")
    private String uploadsPath;

    public SaveRentalPictureService() {}

    public String execute(MultipartFile file) {
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
