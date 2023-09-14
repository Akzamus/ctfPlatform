package com.cycnet.ctfPlatform.services;

import java.io.InputStream;

public interface FileService {

    String uploadFile(String fileName, InputStream inputStream) throws Exception;
    byte[] getFile(String fileName) throws Exception;
    String renameFile(String oldFileName, String newFileName) throws Exception;
    void deleteFile(String fileName) throws Exception;

}
