package com.ets.service;

import com.ets.entity.EtsFile;
import com.ets.model.FileResponse;
import com.ets.repository.EtsFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
//CRUD
public class EtsFileServiceImpl implements EtsFileService {

    @Autowired
    private EtsFileRepository etsFileRepository;

    // Save operation
    @Override
    public EtsFile saveEtsFile(EtsFile etsFile) {
        return etsFileRepository.save(etsFile);
    }

    // Read operation
    @Override
    public List<EtsFile> fetchEtsFileList() {
        return (List<EtsFile>) etsFileRepository.findAll();
    }

    // Read operation by id
    @Override
    public FileResponse fetchEtsFileWithInfo(Long fileId) throws IOException {
        Optional<EtsFile>  etsFileOpt = etsFileRepository.findById(fileId);
        if (etsFileOpt.isPresent()) {
            EtsFile etsFile = etsFileOpt.get();
            FileResponse response = new FileResponse();
            response.setFileInfo(etsFile);
            Path path = Paths.get(etsFile.getPath());
            byte[] byteArray = Files.readAllBytes(path);
            String fileContent = new String(byteArray, StandardCharsets.UTF_8);
            response.setFile(fileContent);
            return response;
        } else {
            throw new IOException("File not found. id: " + fileId);
        }
    }

    // Read operation by id
    @Override
    public EtsFile fetchEtsFile(Long fileId) throws IOException {
        Optional<EtsFile>  etsFileOpt = etsFileRepository.findById(fileId);
        if (etsFileOpt.isPresent()) {
            EtsFile etsFile = etsFileOpt.get();
            return etsFile;
        } else {
            throw new IOException("File not found. id: " + fileId);
        }
    }

    // Update operation
    @Override
    public EtsFile updateEtsFile(EtsFile etsFile, Long fileId) throws IOException {
        Optional<EtsFile> resultOpt = etsFileRepository.findById(fileId);

        if (resultOpt.isEmpty()) {
            throw new IOException("File not found. id: " + fileId);
        }

        EtsFile result = resultOpt.get();

        if (Objects.nonNull(etsFile.getName()) && !"".equalsIgnoreCase(etsFile.getName())) {
            result.setName(etsFile.getName());
        }

        if (Objects.nonNull(etsFile.getPath()) && !"".equalsIgnoreCase(etsFile.getPath())) {
            result.setPath(etsFile.getPath());
        }

        if (Objects.nonNull(etsFile.getSize())) {
            result.setSize(etsFile.getSize());
        }

        if (Objects.nonNull(etsFile.getExtension())) {
            result.setExtension(etsFile.getExtension());
        }

        if (Objects.nonNull(etsFile.getUpdatedy())) {
            result.setUpdatedy(etsFile.getUpdatedy());
        }

        if (Objects.nonNull(etsFile.getUpddate())) {
            result.setUpddate(etsFile.getUpddate());
        }

        return etsFileRepository.save(result);
    }

    // Delete operation
    @Override
    public void deleteEtsFileById(Long fileId) throws IOException {
        Optional<EtsFile> resultOpt = etsFileRepository.findById(fileId);
        if (resultOpt.isPresent()) {
            EtsFile result = resultOpt.get();
            if (Objects.nonNull(result.getPath())) {
                try {
                    Path path = Paths.get(result.getPath());
                    Files.delete(path);
                    etsFileRepository.deleteById(fileId);
                } catch (IOException ioe) {
                    throw new IOException("Could not delete file. id: " + fileId, ioe);
                }
            }
        } else {
            throw new IOException("File not found. id: " + fileId);
        }
    }
}
