package com.ets.util;

import com.ets.config.Config;
import com.ets.entity.EtsFile;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class FileUtil {

    public static EtsFile saveFile(String fileName, MultipartFile multipartFile, boolean isUpdateOrNot, EtsFile updateFile) throws IOException {

        EtsFile etsFile = new EtsFile();
        long size = 0;
        int chunk = 0;

        String fileExtension = StringUtils.getFilenameExtension(fileName);
        List<String> validExt = Arrays.asList(Config.VALID_EXTENSIONS_STATIC);
        if (!validExt.contains(fileExtension.toLowerCase(Locale.ROOT))) {
            throw new IOException("The file has invalid extension: " + fileName);
        }

        Path uploadPath = Paths.get("EtsFiles").toAbsolutePath();

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            inputStream.transferTo(baos);
            InputStream firstClone = new ByteArrayInputStream(baos.toByteArray());
            InputStream secondClone = new ByteArrayInputStream(baos.toByteArray());

            byte[] buffer = new byte[1024];
            while ((chunk = firstClone.read(buffer)) != -1) {
                size += chunk;
            }
            long mbSize = size / (1024 * 1024);
            if (mbSize > Long.valueOf(Config.SIZE_LIMIT_STATIC)) {
                throw new IOException("Maximum 5MB files can be uploaded. The size of your file is " + mbSize + "MB");
            }

            String fileCode = RandomStringUtils.randomAlphanumeric(8);
            Path filePath = uploadPath.resolve(fileCode + "_" + fileName);
            Files.copy(secondClone, filePath, StandardCopyOption.REPLACE_EXISTING);
            etsFile.setPath(String.valueOf(filePath));
            etsFile.setName(fileName);
            if (!isUpdateOrNot) {
                etsFile.setCdate(LocalDate.now());
                etsFile.setCreatedby(System.getProperty("user.name"));
            } else {
                Path updateFilePath = Paths.get(updateFile.getPath());
                Files.delete(updateFilePath);
                etsFile.setUpddate(LocalDate.now());
                etsFile.setUpdatedy(System.getProperty("user.name"));
            }

            etsFile.setExtension(fileExtension);
            etsFile.setSize(size);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }

        return etsFile;
    }
}
