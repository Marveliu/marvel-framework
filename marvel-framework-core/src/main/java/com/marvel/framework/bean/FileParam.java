package com.marvel.framework.bean;

import java.io.InputStream;

/**
 * 文件表单参数
 *
 * @author Marveliu
 * @since 17/04/2018
 **/

public class FileParam {
    // 文件表单字段名称
    private String fieldName;
    // 文件名称
    private String fileName;
    // 文件大小
    private long fileSize;
    // 文件类型
    private String contentType;
    // 输入流
    private InputStream inputStream;


    public FileParam(String fieldName, String fileName, long fileSize, String contentType, InputStream inputStream) {
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.inputStream = inputStream;
    }


    public String getFieldName() {
        return fieldName;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
