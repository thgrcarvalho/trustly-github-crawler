package com.challenge.GithubCrawler.model;

public class TextFile {
    private String fileExtension;
    private Integer linesQty;
    private Integer size;

    public TextFile(String fileExtension, Integer linesQty, Integer size) {
        this.fileExtension = fileExtension;
        this.linesQty = linesQty;
        this.size = size;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Integer getLinesQty() {
        return linesQty;
    }

    public void setLinesQty(Integer linesQty) {
        this.linesQty = linesQty;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "File{" +
                "linesQty=" + linesQty +
                ", size=" + size +
                '}';
    }
}
