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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

        /*assertThrows(EntityNotFoundException.class, () -> {
            albumService.getAlbumFindByName("다른 이름");
        } );*/

        AlbumDto resAlbum = albumService.getAlbumFindByName(savedAlbum.getAlbumName());
        assertEquals("이름조회 테스트", resAlbum.getAlbumName());

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


    @Test
    void testAlbumCreate() throws Exception {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumName("createdAlbum");
        int total = albumRepository.findAll().size();
        AlbumDto savedAlbumDto = albumService.createAlbum(albumDto);
        assertEquals(total+1, albumRepository.findAll().size());
    }

    @Test
    void testAlbumDelete() throws Exception{
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumName("createdAlbum");
        AlbumDto savedAlbumDto = albumService.createAlbum(albumDto);

        Files.delete(Paths.get(Constants.PATH_PREFIX + "/photos/original/" + savedAlbumDto.getAlbumId()));
        Files.delete(Paths.get(Constants.PATH_PREFIX + "/photos/thumb/" + savedAlbumDto.getAlbumId()));

        assertEquals(false, Files.exists(Paths.get(Constants.PATH_PREFIX + "/photos/original/" + savedAlbumDto.getAlbumId())));
        assertEquals(false, Files.exists(Paths.get(Constants.PATH_PREFIX + "/photos/thumb/" + savedAlbumDto.getAlbumId())));
    }
}