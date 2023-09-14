package com.cycnet.ctfPlatform.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FileUtils {

    public static String addTimestampToFileName(String fileName, ZoneId zoneId) {
        String formattedTime = getFormattedTime(zoneId);
        int dotIndex = fileName.lastIndexOf(".");

        if (dotIndex >= 0) {
            String fileNameWithoutExtension = fileName.substring(0, dotIndex);
            String fileExtension = fileName.substring(dotIndex);
            return fileNameWithoutExtension + "_" + formattedTime + fileExtension;
        }

        return fileName + "_" + formattedTime;
    }

    private static String getFormattedTime(ZoneId zoneId) {
        ZonedDateTime currentTime = ZonedDateTime.now(zoneId);
        return currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
