package com.cycnet.ctfPlatform.services;

import java.io.InputStream;

public interface StorageService {
    String uploadFile(String filePath, InputStream inputStream);
    byte[] getFile(String filePath);
    String renameFile(String oldPath, String newPath);
    void deleteFile(String path);

}
