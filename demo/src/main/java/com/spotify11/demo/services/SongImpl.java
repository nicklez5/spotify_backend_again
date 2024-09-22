package com.spotify11.demo.services;

import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.User;

import com.spotify11.demo.exception.FileStorageException;
import com.spotify11.demo.exception.MentionedFileNotFoundException;
import com.spotify11.demo.exception.SongException;

import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.property.FileStorageProperties;
import com.spotify11.demo.repo.LibraryRepo;
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.repo.UserRepository;
import com.spotify11.demo.response.UploadFileResponse;
import com.spotify11.demo.utilites.Functions;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


@Service
public class SongImpl implements SongService {

    private static final Logger log = LoggerFactory.getLogger(SongImpl.class);

    private final UserRepository userRepo;

    private final Path fileStorageLocation;

    private final SongRepo songRepo;

    @Autowired
    private FileSystemStorageService storageService;

    public Functions functions;
    private final Path UPLOAD_PATH = Paths.get(new ClassPathResource("").getFile().getAbsolutePath() + File.separator + "static");

    public MultipartFile file;
    @Autowired
    private LibraryRepo libraryRepo;


    public SongImpl(UserRepository userRepo, SongRepo songRepo, FileStorageProperties fileStorageProperties) throws IOException {
        this.songRepo = songRepo;
        this.userRepo = userRepo;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try{
            Files.createDirectories(this.fileStorageLocation);
        }catch(Exception e){
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", e);
        }

    }

    @Transactional
    @Override
    public UploadFileResponse createSong(String title, String artist, String email, MultipartFile file123) throws Exception {
        if (userRepo.findByEmail(email).isPresent()) {
            User user = userRepo.findByEmail(email).get();
            String fileName = StringUtils.cleanPath(file123.getOriginalFilename());

            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            Files.copy(file123.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/songs/").toUriString();

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/songs/download/").path(fileName).toUriString();
            Song song123 = new Song(title,artist,fileDownloadUri,fileUri);
            user.getLibrary().addSong(song123,email);
            songRepo.save(song123);
            UploadFileResponse upload_me = new UploadFileResponse(song123.getId(), file123.getOriginalFilename(),fileDownloadUri, file123.getContentType(), file123.getSize());
            return upload_me;
        }else{
            throw new UserException("User not found");
        }

    }
    @Transactional
    @Override
    public Song updateSong(String title, String artist ,Integer song_id, String email) throws UserException {
        if (userRepo.findByEmail(email).isPresent()) {
            User user = userRepo.findByEmail(email).get();
            List<Song> xyz = user.getLibrary().getSongs();
            for (Song song : xyz) {
                if (song.getId() == song_id) {
                    song.setArtist(artist);
                    song.setTitle(title);
                    songRepo.save(song);
                    return song;
                }
            }
        }else{
            throw new UserException("User not found");
        }

        return null;
    }

    @Transactional
    @Override
    public Resource loadFileAsResource(String fileName) throws MentionedFileNotFoundException, FileNotFoundException {
        try{
            Path filePath = Functions.basePath.resolve
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            }else{
                throw new FileNotFoundException("File not found " + fileName);
            }
        }catch(MalformedURLException | FileNotFoundException ex){
            throw new FileNotFoundException("File not found" + fileName);
        }
    }

    @Override
    public File multipartFile(MultipartFile file, String fileName) throws IOException {
        File convFile = new File(basePath + File.separator + fileName);
        file.transferTo(convFile);
        return convFile;
    }



    @Transactional
    public Song deleteSong(int song_id, String email) throws SongException {

            if(userRepo.findByEmail(email).isPresent()){
                User user = userRepo.findByEmail(email).get();
                List<Song> xyz = user.getLibrary().getSongs();
                for(Song song : xyz){
                    if(song.getId() == song_id){
                        user.getLibrary().removeSong(song);
                        userRepo.save(user);
                        songRepo.delete(song);
                        songRepo.saveAndFlush(song);
                        return song;
                    }
                }
            }else{
                throw new SongException("Song does not exist");
            }

            return null;

    }


    public Song getSong(int song_id, String email) throws SongException {
        if (userRepo.findByEmail(email).isPresent()) {
            User user = userRepo.findByEmail(email).get();

            List<Song> xyz = user.getLibrary().getSongs();
            for (Song song : xyz) {
                if (song.getId() == song_id) {
                    return this.songRepo.findById(song_id).get();

                }
            }
            throw new SongException("Song id:" + song_id + " has not been found");

        }
        return null;
    }

    public Song getSong(String title, String email) throws  SongException {
        if(userRepo.findByEmail(email).isPresent()) {
            User user = userRepo.findByEmail(email).get();
            List<Song> xyz = user.getLibrary().getSongs();
            for (Song song : xyz) {
                if (song.getTitle().equals(title)) {
                    return this.songRepo.findByTitle(title).get();
                }
            }
        }

            throw new SongException("Song title: " + title + " could not be found");

    }


    public List<Song> getAllSongs(String email) throws UserException {
        if(userRepo.findByEmail(email).isPresent()){
            User user = userRepo.findByEmail(email).get();
            return user.getLibrary().getSongs();
        }else{
            throw new UserException("User does not exist");
        }
    }
}