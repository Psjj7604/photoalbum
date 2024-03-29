package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.mapper.AlbumMapper;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private PhotoRepository photoRepository;

    public AlbumDto getAlbumFindById(Long albumId){
        Optional<Album> res = albumRepository.findById(albumId);
        if(res.isPresent()){
            AlbumDto albumDto = AlbumMapper.convertToDto(res.get());
            albumDto.setCount(photoRepository.countByAlbum_AlbumId(albumId));
            return albumDto;
        }
        else{
            throw new EntityNotFoundException(String.format("앨범 아이디 %d로 조회되지 않음", albumId));
        }

    }

    public AlbumDto getAlbumFindByName(String albumName){
        Optional<Album> res = albumRepository.findByAlbumName(albumName);
        if(res.isPresent()){
            AlbumDto albumDto = AlbumMapper.convertToDto(res.get());
            albumDto.setCount(photoRepository.countByAlbum_AlbumName(albumName)); //미완
            return albumDto;
        }
        else{
            throw new EntityNotFoundException(String.format("앨범 이름 %s로 조회되지 않음", albumName));
        }
    }

    public AlbumDto createAlbum(AlbumDto albumDto) throws IOException {
        Album album = AlbumMapper.convertToModel(albumDto);
        this.albumRepository.save(album);
        this.createAlbumDirectories(album);
        return AlbumMapper.convertToDto(album);
    }

    private void createAlbumDirectories(Album album) throws IOException {
        Files.createDirectories(Paths.get(Constants.PATH_PREFIX + "/photos/original/" + album.getAlbumId()));
        Files.createDirectories(Paths.get(Constants.PATH_PREFIX + "/photos/thumb/" + album.getAlbumId()));
    }

    private void deleteAlbumDirectories(Album album) throws IOException{
        Files.delete(Paths.get(Constants.PATH_PREFIX + "/photos/original/" + album.getAlbumId()));
        Files.delete(Paths.get(Constants.PATH_PREFIX + "/photos/thumb/" + album.getAlbumId()));
    }

    public List<AlbumDto> getAlbumList(String keyword, String sort, String orderBy){
        List<Album> albums;
        if (!Objects.equals(orderBy, "asc") && !Objects.equals(orderBy, "desc")) {
            throw new IllegalArgumentException("올바른 정렬 순서를 지정해주세요. (asc 또는 desc)");
        }

        if (Objects.equals(sort, "byName")) {
            if(Objects.equals(orderBy, "asc")){
                albums = albumRepository.findByAlbumNameContainingOrderByAlbumNameAsc(keyword);
            }
            else {
                albums = albumRepository.findByAlbumNameContainingOrderByAlbumNameDesc(keyword);
            }

        } else if (Objects.equals(sort, "byDate")) {
            if (Objects.equals(orderBy,"asc")){
                albums = albumRepository.findByAlbumNameContainingOrderByCreatedAtAsc(keyword);
            }
            else {
                albums = albumRepository.findByAlbumNameContainingOrderByCreatedAtDesc(keyword);
            }
        } else {
            throw new IllegalStateException("알 수 없는 정렬 기준입니다.");
        }
        List<AlbumDto> albumDtos = AlbumMapper.converToDtoList(albums);

        for (AlbumDto albumDto : albumDtos) {
            List<Photo> top4 = photoRepository.findTop4ByAlbum_AlbumIdOrderByUploadedAtDesc(albumDto.getAlbumId());
            albumDto.setThumbUrls(top4.stream().map(Photo::getThumbUrl).map(c -> Constants.PATH_PREFIX + c).collect(Collectors.toList()));
        }
        return albumDtos;
    }

    public AlbumDto changeName(Long AlbumId, AlbumDto albumDto){
        Optional<Album> album = this.albumRepository.findById(AlbumId);
        if(album.isEmpty()){
            throw new NoSuchElementException(String.format("Album ID : '%d'가 존재하지 않습니다", AlbumId));
        }

        Album updateAlbum = album.get();
        updateAlbum.setAlbumName(albumDto.getAlbumName());
        Album savedAlbum = this.albumRepository.save(updateAlbum);
        return AlbumMapper.convertToDto(savedAlbum);
    }

    public void deleteAlbum(Long AlbumId) throws IOException {
        Optional<Album> album = this.albumRepository.findById(AlbumId);
        if(album.isEmpty()){
            throw new NoSuchElementException(String.format("Album ID : '%d'가 존재하지 않흡니다", AlbumId));
        }
        Album deleteAlbum = album.get();
        albumRepository.deleteById(deleteAlbum.getAlbumId());
        deleteAlbumDirectories(deleteAlbum);
    }
}
