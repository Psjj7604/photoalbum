package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AlbumServiceTest {

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    AlbumService albumService;

    @Test
    void getAlbum() {
        Album album = new Album();
        album.setAlbumName("테스트12");
        Album savedAlbum = albumRepository.save(album);

        AlbumDto resAlbum = albumService.getAlbumFindById(savedAlbum.getAlbumId());
        assertEquals("테스트12", resAlbum.getAlbumName());
    }

    @Test
    void getAlbumFindByName() {
        Album album = new Album();
        album.setAlbumName("이름조회 테스트");
        Album savedAlbum = albumRepository.save(album);

        /*assertThrows(EntityNotFoundException.class, () -> {
            albumService.getAlbumFindByName("이름조회 테스트");
        } );*/

        assertThrows(EntityNotFoundException.class, () -> {
            albumService.getAlbumFindByName("다른 이름");
        } );

        //Album resAlbum = albumService.getAlbumFindByName(savedAlbum.getAlbumName());
        //assertEquals("이름조회 테스트", resAlbum.getAlbumName());

    }

    @Test
    void testPhotoCount(){
        Album album = new Album();
        album.setAlbumName("count 테스트");
        Album savedAlbum = albumRepository.save(album);

        Photo photo1 = new Photo();
        photo1.setFileName("사진1");
        photo1.setAlbum(savedAlbum);
        photoRepository.save(photo1);

        Photo photo2 = new Photo();
        photo2.setFileName("사진2");
        photo2.setAlbum(savedAlbum);
        photoRepository.save(photo2);


        AlbumDto resAlbum = albumService.getAlbumFindById(savedAlbum.getAlbumId());
        assertEquals(2, resAlbum.getCount());

    }

}