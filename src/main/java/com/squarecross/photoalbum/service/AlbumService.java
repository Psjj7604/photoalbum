package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;

    public Album getAlbumFindById(Long albumId){
        Optional<Album> res = albumRepository.findById(albumId);
        if(res.isPresent()){
            return res.get();
        }
        else{
            throw new EntityNotFoundException(String.format("앨범 아이디 %d로 조회되지 않음", albumId));
        }

    }

    public Album getAlbumFindByName(String albumName){
        Optional<Album> res = albumRepository.findByAlbumName(albumName);
        if(res.isPresent()){
            return res.get();
        }
        else{
            throw new EntityNotFoundException(String.format("앨범 이름 %s로 조회되지 않음", albumName));
        }
    }
}
