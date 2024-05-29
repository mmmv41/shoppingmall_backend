package com.github.shopping_mall_be.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Component
public class FileStorageUtil {

    @Value("${upload.dir}")
    private String uploadDir;

    // 최대 이미지 개수는 10개로 지정
    private static final int MAX_IMAGE_COUNT = 10;

    // 현재 저장된 이미지 개수를 저장할 변수 (예시로 메모리 내에서 관리)
    private int currentImageCount = 0;

    public synchronized String storeFile(byte[] fileData) throws IOException {
        if (fileData == null || fileData.length == 0) {
            throw new IllegalArgumentException("File data is empty");
        }

        if (isMaxImageCountReached()) {
            throw new IllegalStateException("Maximum image count reached");
        }

        // Generate random file name to prevent duplicate file names
        String fileName = UUID.randomUUID().toString() + ".jpg"; // 확장자는 필요에 따라 조정 가능
        Path copyLocation = Paths.get(uploadDir + File.separator + fileName);
        Files.write(copyLocation, fileData);

        // 이미지 저장 후 현재 이미지 개수 증가
        currentImageCount++;

        return fileName;
    }

    public String storeFile(String base64File, String filename) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(base64File);
        Path filePath = Paths.get(uploadDir, filename).normalize();
        Files.write(filePath, decodedBytes);
        return filename;
    }



//    public List<String> storeMultipleFiles(List<MultipartFile> files) throws IOException {
//        List<String> fileUrls = new ArrayList<>();
//        for (MultipartFile file : files) {
//            if (!file.isEmpty()) {
//                if (!isMaxImageCountReached()) {
//                    String fileName = storeFile(file);
//                    fileUrls.add(fileName);
//                } else {
//                    throw new IllegalStateException("Maximum image count reached");
//                }
//            }
//        }
//        return fileUrls;
//    }

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
