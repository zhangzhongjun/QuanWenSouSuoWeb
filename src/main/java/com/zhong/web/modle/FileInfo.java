package com.zhong.web.modle;

import java.util.Date;

/**
 * @author 张中俊
 **/
public class FileInfo {
    private String id;
    private String filename;
    private String filepath;
    private Date uploadtime;
    private int userid;
    private boolean isdeleted;

    public FileInfo(String id, String filename, String filepath, Date uploadtime, int userid, boolean isdeleted) {
        this.id = id;
        this.filename = filename;
        this.filepath = filepath;
        this.uploadtime = uploadtime;
        this.userid = userid;
        this.isdeleted = isdeleted;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("==========正在查看文件信息==============");
        sb.append(System.lineSeparator());
        sb.append("id: " + id);
        sb.append(System.lineSeparator());
        sb.append("filename: " + filename);
        sb.append(System.lineSeparator());
        sb.append("filepath: " + filepath);
        sb.append(System.lineSeparator());
        sb.append("uploadtime: " + uploadtime);
        sb.append(System.lineSeparator());
        sb.append("userid: " + userid);
        sb.append(System.lineSeparator());
        sb.append("isdeleted: " + isdeleted);
        sb.append(System.lineSeparator());
        sb.append("==============================");
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public Date getUploadtime() {
        return uploadtime;
    }

    public int getUserid() {
        return userid;
    }

    public boolean isIsdeleted() {
        return isdeleted;
    }
}
