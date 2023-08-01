package com.squarecross.photoalbum.dto;

import java.util.Date;

public class PhotoDto {
    private Long photoId;
    private String fileName;
    private int fileSize;
    private String originalUrl;
    private String thumbUrl;
    private Date UploadedAt;
    private Long albumId;

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Date getUploadedAt() {
        return UploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        UploadedAt = uploadedAt;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }


}
