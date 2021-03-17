package com.challenge.GithubCrawler.model;

public class FileExtensionData {
    private String fileExtension;
    private Integer accumulatedFileLinesQty;
    private Integer accumulatedFileSize;

    public FileExtensionData(String fileExtension, Integer accumulatedFileLinesQty, Integer accumulatedFileSize) {
        this.fileExtension = fileExtension;
        this.accumulatedFileLinesQty = accumulatedFileLinesQty;
        this.accumulatedFileSize = accumulatedFileSize;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Integer getAccumulatedFileLinesQty() {
        return accumulatedFileLinesQty;
    }

    public void setAccumulatedFileLinesQty(Integer accumulatedFileLinesQty) {
        this.accumulatedFileLinesQty = accumulatedFileLinesQty;
    }

    public Integer getAccumulatedFileSize() {
        return accumulatedFileSize;
    }

    public void setAccumulatedFileSize(Integer accumulatedFileSize) {
        this.accumulatedFileSize = accumulatedFileSize;
    }

    @Override
    public String toString() {
        return "FileExtensionData{" +
                "fileExtension='" + fileExtension + '\'' +
                ", accumulatedFileLinesQty=" + accumulatedFileLinesQty +
                ", accumulatedFileSize=" + accumulatedFileSize +
                '}';
    }
}
