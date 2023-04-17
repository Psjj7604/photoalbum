package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.repository.AlbumRepository;
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
    AlbumService albumService;

    @Test
    void getAlbum() {
        Album album = new Album();
        album.setAlbumName("테스트12");
        Album savedAlbum = albumRepository.save(album);

        Album resAlbum = albumService.getAlbumFindById(savedAlbum.getAlbumId());
        assertEquals("테스트12", resAlbum.getAlbumName());
    }

    @Test
    void getAlbumAlbumFindByName() {
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


}