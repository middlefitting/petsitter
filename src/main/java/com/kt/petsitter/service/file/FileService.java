package com.kt.petsitter.service.file;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    // resources/static 하위에서 uploads 폴더를 찾을 경로
    private static final String UPLOAD_DIR = "static/uploads";

    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        // resources/static/uploads 실제 파일 경로
        File resourceDir = new ClassPathResource(UPLOAD_DIR).getFile();
        Path uploadPath = Paths.get(resourceDir.getAbsolutePath());

        // 랜덤 파일 이름 (UUID)
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String savedFileName = UUID.randomUUID().toString() + extension;

        // 파일 생성
        File savedFile = new File(uploadPath.toFile(), savedFileName);
        file.transferTo(savedFile);  // 여기에 쓰기 시도

        // 정적 리소스로 접근할 때:  /uploads/파일명
        return "/api/uploads/" + savedFileName;
    }
}
