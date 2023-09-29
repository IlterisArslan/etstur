package com.ets.model;

import com.ets.entity.EtsFile;

public class FileResponse {
    private EtsFile fileInfo;
    private String file;

    public EtsFile getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(EtsFile fileInfo) {
        this.fileInfo = fileInfo;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
