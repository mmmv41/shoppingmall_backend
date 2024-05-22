package com.github.shopping_mall_be.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

// 이미지를 저장하는 유틸리티 클래스
@Component
public class FileStorageUtil {

    @Value("${upload.dir}")
    private String uploadDir;

    // 최대 이미지 개수는 10개로 지정
    private static final int MAX_IMAGE_COUNT = 10;

    public String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (isMaxImageCountReached()) {
            throw new IllegalStateException("Maximum image count reached");
        }

        // Generate random file name to prevent duplicate file names
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path copyLocation = Paths.get(uploadDir + File.separator + fileName);
        Files.copy(file.getInputStream(), copyLocation);

        return fileName;
    }

    private boolean isMaxImageCountReached() {
        File directory = new File(uploadDir);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                return files.length >= MAX_IMAGE_COUNT;
            }
        }
        return false;
    }
}
