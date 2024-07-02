package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FileService {

    private final String UPLOAD_DIR = "/static/upload/avatar/";

    public Map<String, String> uploadAvatar(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("保存失败");
        }

        String fileType = file.getContentType();
        int spliceLength = fileType.lastIndexOf("/");
        String fileTypeText = fileType.substring(spliceLength + 1);
        String fileName = UUID.randomUUID().toString().replace("-", "") + "." + fileTypeText;
        String filePath = UPLOAD_DIR + fileName;

        Files.copy(file.getInputStream(), Paths.get("www" + filePath), StandardCopyOption.REPLACE_EXISTING);

        Map<String, String> response = new HashMap<>();
        response.put("name", fileName);
        response.put("fileUrl", filePath);

        return response;
    }
}
