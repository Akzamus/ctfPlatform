package com.cycnet.ctfPlatform.utils;

import com.cycnet.ctfPlatform.exceptions.server.InternalServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileUtils {

    public static String generateFileName(MultipartFile multipartFile) {
        return multipartFile.getOriginalFilename() == null
                ? UUID.randomUUID().toString()
                : convertToCamelCase(multipartFile.getOriginalFilename());
    }

    public static String convertToCamelCase(String fileName) {
        return Arrays.stream(fileName.split(" "))
                .filter(word -> !word.isEmpty())
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining());
    }

    public static InputStream getInputStreamOrElseThrow(MultipartFile multipartFile) {
        try {
            return multipartFile.getInputStream();
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }
}
