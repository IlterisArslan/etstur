package com.ets.controller;

import com.ets.entity.EtsFile;
import com.ets.model.FileResponse;
import com.ets.service.EtsFileService;
import com.ets.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

// Annotation
@RestController

// Class
@RequestMapping("ets/api")
public class EtsFileController {

    @Autowired
    private EtsFileService etsFileService;

    // Save operation
    @PostMapping("/file")
    public EtsFile saveEtsFile(
            @RequestParam("file") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();

        EtsFile etsFile = FileUtil.saveFile(fileName, multipartFile, false, null);

        return etsFileService.saveEtsFile(etsFile);
    }

    // Read operation
    @GetMapping("files")
    public List<EtsFile> fetchEtsFileList() {
        return etsFileService.fetchEtsFileList();
    }

    // Read operation by id
    @GetMapping("file/{id}")
    public ResponseEntity<FileResponse> fetchEtsFile(@PathVariable("id") Long fileId) throws IOException {
        FileResponse response = etsFileService.fetchEtsFileWithInfo(fileId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Update operation
    @PutMapping("file/{id}")
    public String
    updateEtsFile(@RequestParam("file") MultipartFile multipartFile,
                  @PathVariable("id") Long fileId) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        EtsFile etsFile = etsFileService.fetchEtsFile(fileId);
        try {
            EtsFile updatedEtsFile = FileUtil.saveFile(fileName, multipartFile, true, etsFile);
            etsFileService.updateEtsFile(
                    updatedEtsFile, fileId);
            return "Updated Successfully";
        } catch (IOException ioe) {
            throw new IOException("Could not update file. id: " + fileId, ioe);
        }
    }

    // Delete operation
    @DeleteMapping("file/{id}")
    public String deleteEtsFileById(@PathVariable("id")
                                            Long fileId) throws IOException {
        etsFileService.deleteEtsFileById(
                fileId);
        return "Deleted Successfully";
    }
}
