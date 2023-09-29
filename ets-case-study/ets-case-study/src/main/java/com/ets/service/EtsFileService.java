package com.ets.service;

import com.ets.entity.EtsFile;
import com.ets.model.FileResponse;

import java.io.IOException;
import java.util.List;
//CRUD
public interface EtsFileService {

    // Save operation
    EtsFile saveEtsFile(EtsFile etsFile);

    // Read operation
    List<EtsFile> fetchEtsFileList();

    // Read operation by id with byteArray
    FileResponse fetchEtsFileWithInfo(Long id) throws IOException;

    // Read operation by id without byteArray
    EtsFile fetchEtsFile(Long id) throws IOException;

    // Update operation
    EtsFile updateEtsFile(EtsFile etsFile, Long id) throws IOException;

    // Delete operation
    void deleteEtsFileById(Long id) throws IOException;
}
