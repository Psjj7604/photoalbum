package com.squarecross.photoalbum.mapper;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.dto.PhotoDto;

import java.util.List;
import java.util.stream.Collectors;

public class PhotoMapper {
    public static PhotoDto convertToDto(Photo photo) {
        PhotoDto photoDto = new PhotoDto();
        photoDto.setPhotoId(photo.getPhotoId());
        photoDto.setFileSize(photo.getFileSize());
        photoDto.setFileName(photo.getFileName());
        photoDto.setOriginalUrl(photo.getOriginalUrl());
        photoDto.setThumbUrl(photo.getThumbUrl());
        photoDto.setUploadedAt(photo.getUploadedAt());
        photoDto.setAlbumId(photo.getAlbum().getAlbumId());
        return photoDto;
    }

    public static Photo convertToModel(PhotoDto photoDto) {
        Photo photo = new Photo();
        photo.setPhotoId(photoDto.getPhotoId());
        photo.setFileName(photoDto.getFileName());
        photo.setFileSize(photoDto.getFileSize());
        photo.setOriginalUrl(photoDto.getOriginalUrl());
        photo.setThumbUrl(photoDto.getThumbUrl());
        photo.setUploadedAt(photoDto.getUploadedAt());
        //photo.setAlbum(photoDto.);
        return photo;
    }


    public static List<PhotoDto> converToDtoList(List<Photo> photos) {
        return photos.stream().map(PhotoMapper::convertToDto).collect(Collectors.toList());
        //photos에 있는 각 사진을 PhotoMapper.converToDto로 변화시킨 이후 리스트로 다시 collect
    }
}
