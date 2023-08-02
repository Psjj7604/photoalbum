package com.squarecross.photoalbum.dto;

import java.util.List;

public class PhotoMoveDto {
    private Long fromAlbumId;
    private Long toAlbumId;

    public Long getFromAlbumId() {
        return fromAlbumId;
    }

    public void setFromAlbumId(Long fromAlbumId) {
        this.fromAlbumId = fromAlbumId;
    }

    public Long getToAlbumId() {
        return toAlbumId;
    }

    public void setToAlbumId(Long toAlbumId) {
        this.toAlbumId = toAlbumId;
    }

    public List<Long> getPhotoIds() {
        return photoIds;
    }

    public void setPhotoIds(List<Long> photoIds) {
        this.photoIds = photoIds;
    }

    private List<Long> photoIds;
}

