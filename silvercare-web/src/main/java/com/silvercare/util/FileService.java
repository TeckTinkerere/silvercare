package com.silvercare.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import jakarta.servlet.http.Part;

/**
 * Utility for handling secure file uploads in the web application.
 */
public class FileService {

    private static final String UPLOAD_DIR = "uploads/images";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public static String uploadImage(Part filePart, String contextPath) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        if (filePart.getSize() > MAX_FILE_SIZE) {
            throw new IOException("File size exceeds limit.");
        }

        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String extension = fileName.substring(fileName.lastIndexOf('.'));

        // Validate MIME type
        String contentType = filePart.getContentType();
        if (!contentType.startsWith("image/")) {
            throw new IOException("Invalid file type. Only images allowed.");
        }

        String safeFileName = UUID.randomUUID().toString() + extension;
        String uploadPath = contextPath + File.separator + UPLOAD_DIR;

        File uploadFolder = new File(uploadPath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        Path filePath = Paths.get(uploadPath, safeFileName);
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return UPLOAD_DIR + "/" + safeFileName;
    }
}
