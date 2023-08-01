package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class PhotoServiceTest {
    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    AlbumService albumService;
    @Autowired
    PhotoService photoService;


    @Test
    void getPhotoFindById() throws Exception {
        Photo photo = new Photo();
        photo.setFileName("테스트1");
        Photo savedPhoto = photoRepository.save(photo);

        PhotoDto resPhoto = photoService.getPhotoFindById(savedPhoto.getPhotoId());
        assertEquals("테스트1", resPhoto.getFileName());

    }

    @Test
    void 사진정보상세조회() throws Exception{


    }

}