package com.cycnet.ctfPlatform.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface StorageService {

    String uploadFile(MultipartFile multipartFile);
    String uploadFile(String fileName, InputStream inputStream);
    byte[] getFile(String fileName);
    String renameFile(String oldFileName, String newFileName);
    void deleteFile(String fileName);

}
