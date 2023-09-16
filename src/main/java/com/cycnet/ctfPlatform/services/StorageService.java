package com.cycnet.ctfPlatform.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface StorageService {

    String uploadFile(MultipartFile multipartFile);
    String uploadFile(String path, InputStream inputStream);
    byte[] getFile(String path);
    String renameFile(String oldPath, String newPath);
    void deleteFile(String path);

}
