package com.squarecross.photoalbum.repository;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    int countByAlbum_AlbumId(Long AlbumId);
    int countByAlbum_AlbumName(String AlbumName);

    List<Photo> findTop4ByAlbum_AlbumIdOrderByUploadedAtDesc(Long AlbumId);

    Optional<Photo> findByFileNameAndAlbum_AlbumId(String photoName, Long albumId);

    Optional<Photo> findByAlbum_AlbumIdAndPhotoId(Long albumId, Long photoId);

    List<Photo> findByAlbum_AlbumIdAndFileNameContainingOrderByUploadedAtDesc(Long albumId, String keyword);
    List<Photo> findByAlbum_AlbumIdAndFileNameContainingOrderByUploadedAtAsc(Long albumId, String keyword);
    List<Photo> findByAlbum_AlbumIdAndFileNameContainingOrderByFileNameAsc(Long albumId, String keyword);
    List<Photo> findByAlbum_AlbumIdAndFileNameContainingOrderByFileNameDesc(Long albumId, String keyword);



}
