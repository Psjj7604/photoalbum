package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.dto.PhotoMoveDto;
import com.squarecross.photoalbum.mapper.AlbumMapper;
import com.squarecross.photoalbum.mapper.PhotoMapper;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhotoService {

    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private PhotoRepository photoRepository;

    private final String original_path = Constants.PATH_PREFIX + "/photos/original";
    private final String thumb_path = Constants.PATH_PREFIX + "/photos/thumb";

    public PhotoDto getPhotoFindById(Long albumId, Long photoId){
        Optional<Photo> res = photoRepository.findByAlbum_AlbumIdAndPhotoId(albumId, photoId);
        if(res.isPresent()){
            PhotoDto photoDto = PhotoMapper.convertToDto(res.get());
            return photoDto;
        }
        else{
            throw new EntityNotFoundException(String.format("사진 아이디 %d로 조회되지 않음", photoId));
        }
    }

    public PhotoDto savePhoto(MultipartFile file, Long albumId) throws IOException {
        Optional<Album> res = albumRepository.findById(albumId);
        if(res.isEmpty()){
            throw new EntityNotFoundException("앨범이 존재하지 않습니다.");
        }
        String fileName = file.getOriginalFilename();
        int fileSize = (int) file.getSize();
        fileName = getNextFileName(fileName, albumId);
        saveFile(file, albumId, fileName);

        Photo photo = new Photo();
        photo.setOriginalUrl("/photos/original/" + albumId + "/" + fileName);
        photo.setThumbUrl("/photos/thumb/" + albumId + "/" + fileName);
        photo.setFileName(fileName);
        photo.setFileSize(fileSize);
        photo.setAlbum(res.get());
        Photo createdPhoto = photoRepository.save(photo);
        return PhotoMapper.convertToDto(createdPhoto);
    }

    private String getNextFileName(String fileName, Long albumId){
        String fileNameNoExt = StringUtils.stripFilenameExtension(fileName); //확장자를 제외한 경로+파일이름
        String ext = StringUtils.getFilenameExtension(fileName); //확장자
        Optional<Photo> res = photoRepository.findByFileNameAndAlbum_AlbumId(fileName, albumId);

        int count = 2;
        while(res.isPresent()){
            fileName = String.format("%s (%d).%s", fileNameNoExt, count, ext);
            res = photoRepository.findByFileNameAndAlbum_AlbumId(fileName, albumId);
            count++;
        }
        return fileName;
    }

    private void saveFile(MultipartFile file, Long AlbumId, String fileName) throws IOException {
        try{
            String filePath = AlbumId+"/"+fileName;
            Files.copy(file.getInputStream(), Paths.get(original_path + "/" + filePath));

            BufferedImage thumbImg = Scalr.resize(ImageIO.read(file.getInputStream()), Constants.THUMB_SIZE, Constants.THUMB_SIZE);
            File thumbFile = new File(thumb_path + "/" + filePath);
            String ext = StringUtils.getFilenameExtension(fileName);
            if(ext == null){
                throw new IllegalArgumentException("확장자가 없습니다.");
            }
            ImageIO.write(thumbImg, ext, thumbFile);
        } catch (Exception e){
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public File getImageFile(Long photoId){
        Optional<Photo> res = photoRepository.findById(photoId);
        if(res.isEmpty()){
            throw new EntityNotFoundException(String.format("사진 ID (%d)를 찾을 수 없습니다.", photoId));
        }
        return new File(Constants.PATH_PREFIX + res.get().getOriginalUrl());
    }

    public List<PhotoDto> getPhotoList(Long albumId, String keyword, String sort, String orderBy){
        List<Photo> photos;
        if (!Objects.equals(orderBy, "asc") && !Objects.equals(orderBy, "desc")) {
            throw new IllegalArgumentException("올바른 정렬 순서를 지정해주세요. (asc 또는 desc)");
        }

        if (Objects.equals(sort, "byName")) {
            if(Objects.equals(orderBy, "asc")){
                photos = photoRepository.findByAlbum_AlbumIdAndFileNameContainingOrderByFileNameAsc(albumId, keyword);
            }
            else {
                photos = photoRepository.findByAlbum_AlbumIdAndFileNameContainingOrderByFileNameDesc(albumId, keyword);
            }

        } else if (Objects.equals(sort, "byDate")) {
            if (Objects.equals(orderBy,"asc")){
                photos = photoRepository.findByAlbum_AlbumIdAndFileNameContainingOrderByUploadedAtAsc(albumId, keyword);
            }
            else {
                photos = photoRepository.findByAlbum_AlbumIdAndFileNameContainingOrderByUploadedAtDesc(albumId, keyword);
            }
        } else {
            throw new IllegalStateException("알 수 없는 정렬 기준입니다.");
        }
        List<PhotoDto> photoDtos = PhotoMapper.converToDtoList(photos);

        return photoDtos;
    }

    public void movePhotos(Long fromAlbumId, Long toAlbumId, List<Long> photoIds) {
        Album toAlbum = albumRepository.findById(toAlbumId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Album ID : '%d'가 존재하지 않습니다.", toAlbumId)));

        for (Long photoId : photoIds) {
            Optional<Photo> photo = this.photoRepository.findById(photoId);
            if (photo.isEmpty()) {
                throw new NoSuchElementException(String.format("Photo ID : '%d'가 존재하지 않습니다.", photoId));
            }
            Photo updatePhoto = photo.get();
            updatePhoto.setAlbum(toAlbum);
            photoRepository.save(updatePhoto);
            /*
            파일 디렉터리 수정 코드 필요
             */

        }
    }

}



