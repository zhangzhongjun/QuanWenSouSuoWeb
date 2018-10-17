package com.zhong.web.modle;

public class FileInfo_2 {
    private String filename;
    private boolean indexd;

    public FileInfo_2(String filename, boolean indexd) {
        this.filename = filename;
        this.indexd = indexd;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isIndexd() {
        return indexd;
    }

    public void setIndexd(boolean indexd) {
        this.indexd = indexd;
    }
}
