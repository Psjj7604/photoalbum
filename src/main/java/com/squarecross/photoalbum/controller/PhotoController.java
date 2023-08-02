package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.dto.PhotoMoveDto;
import com.squarecross.photoalbum.service.AlbumService;
import com.squarecross.photoalbum.service.PhotoService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/albums/{albumId}/photos")
public class PhotoController {
    @Autowired
    private AlbumService albumService;
    @Autowired
    private PhotoService photoService;

    @RequestMapping(value = "/{photoId}", method = RequestMethod.GET)
    public ResponseEntity<PhotoDto> getPhotoInfo(@PathVariable("albumId") final Long albumId,
                                                 @PathVariable("photoId") final Long photoId) {
        PhotoDto photo = photoService.getPhotoFindById(albumId, photoId);
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

    //ResponseEntity로 파일 다운로드 구현가능
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadPhotos(@RequestParam("photoIds") Long[] photoIds, HttpServletResponse response){
        try{
            if(photoIds.length == 1){
                File file = photoService.getImageFile(photoIds[0]);
                OutputStream outputStream = response.getOutputStream();
                IOUtils.copy(new FileInputStream(file), outputStream);
                outputStream.close();
            }
            else{
                ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
                FileInputStream fileInputStream;
                for (long id : photoIds) {
                    File file = photoService.getImageFile(id);
                    zipOut.putNextEntry(new ZipEntry(file.getName()));
                    fileInputStream = new FileInputStream(file);
                    StreamUtils.copy(fileInputStream, zipOut);
                    fileInputStream.close();
                    zipOut.closeEntry();
                }
                zipOut.close();
            }
        } catch (FileNotFoundException e){
            throw new RuntimeException("Error");
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<PhotoDto>> getPhotoList(@RequestParam(value = "keyword", required = false, defaultValue = "") final String keyword,
                                                       @RequestParam(value = "sort", required = false, defaultValue = "byDate") final String sort,
                                                       @RequestParam(value = "orderBy", required = false, defaultValue = "desc") final String orderBy,
                                                       @PathVariable("albumId") final Long albumId) {
        List<PhotoDto> photoDtos = photoService.getPhotoList(albumId, keyword, sort, orderBy);
        return new ResponseEntity<>(photoDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/move", method = RequestMethod.PUT)
    public ResponseEntity<String> movePhoto(@RequestBody final PhotoMoveDto photoMoveDto){
        Long fromAlbumId = photoMoveDto.getFromAlbumId();
        Long toAlbumId = photoMoveDto.getToAlbumId();
        List<Long> photoIds = photoMoveDto.getPhotoIds();

        photoService.movePhotos(fromAlbumId, toAlbumId, photoIds);


        /*
        변경 이후 남은 photo들을 반환
         */

        return new ResponseEntity<>("미완", HttpStatus.OK);
    }
}
