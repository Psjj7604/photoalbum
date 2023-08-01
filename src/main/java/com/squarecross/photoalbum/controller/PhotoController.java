package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.service.AlbumService;
import com.squarecross.photoalbum.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/albums/{albumId}/photos")
public class PhotoController {
    @Autowired
    private AlbumService albumService;
    @Autowired
    private PhotoService photoService;

    /**
     * @param albumId 입력된 앨범 아이디에 대해서 처리하지 않아서 사진 아이디만 가지고도 조회가 가능함.
     *                따라서 @RequestMapping("/albums/{albumId}/photos/{photoId}")에서 {albumId}값이 현재 무의미함.
     */
    @RequestMapping(value = "/{photoId}", method = RequestMethod.GET)
    public ResponseEntity<PhotoDto> getPhotoInfo(@PathVariable("albumId") final Long albumId,
                                                 @PathVariable("photoId") final Long photoId) {
        PhotoDto photo = photoService.getPhotoFindById(photoId);
        return new ResponseEntity<>(photo, HttpStatus.OK);
    }

    //사진 업로드 API
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<List<PhotoDto>> uploadPhotos(@PathVariable("albumId") final Long albumId,
                                                       @RequestParam("photos") MultipartFile[] files) throws IOException {
        List<PhotoDto> photos = new ArrayList<>();
        for (MultipartFile file : files) {
            PhotoDto photoDto = photoService.savePhoto(file, albumId);
            photos.add(photoDto);
        }
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }


}
