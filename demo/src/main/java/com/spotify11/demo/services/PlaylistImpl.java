package com.spotify11.demo.services;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.User;

import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.LibraryRepo;
import com.spotify11.demo.repo.PlaylistRepo;
import com.spotify11.demo.repo.SongRepo;

import com.spotify11.demo.repo.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin
@Service
public class PlaylistImpl implements PlaylistService {



    private final UserRepository userRepo;
    private final PlaylistRepo playlistRepo;
    private final SongRepo songRepo;
    private final LibraryRepo libraryRepo;

    public PlaylistImpl(UserRepository userRepo, PlaylistRepo playlistRepo, SongRepo songRepo, LibraryRepo libraryRepo) {
        this.userRepo = userRepo;
        this.playlistRepo = playlistRepo;
        this.songRepo = songRepo;
        this.libraryRepo = libraryRepo;
    }

    @Override
    public String getPlaylistName(String email) throws UserException {
        if(userRepo.findByEmail(email).isPresent()){
            User user = userRepo.findByEmail(email).get();
            Playlist playlist1 = user.getPlaylist();
            return playlist1.getPlaylistName();
        }else{
            throw new UserException("you are not present my lord:" + email);
        }
    }

    @Transactional
    @Override
    public Playlist addSong(int song_id, String email) throws Exception {

        if (userRepo.findByEmail(email).isPresent()) {
            User user = userRepo.findByEmail(email).get();
            songRepo.findById(song_id).ifPresent(song -> {
                user.getPlaylist().getSongs().add(song);
            });
        }else{
            throw new Exception("Homie wasnt here");
        }
        return null;


    }

    @Transactional
    @Override
    public Playlist removeSong(int song_id, String email) throws SongException, UserException {
    if(userRepo.findByEmail(email).isPresent()) {
        User user1 = userRepo.findByEmail(email).get();
        Song song = songRepo.findById(song_id).get();
        user1.getPlaylist().getSongs().remove(song);
        userRepo.save(user1);
        return user1.getPlaylist();


    }else{
        throw new UserException("User does not exist");
    }

}




    @Transactional
    @Override
    public Playlist renamePlaylist(String email, String playlist_name) throws UserException {
            if(userRepo.findByEmail(email).isPresent()) {
                User user = userRepo.findByEmail(email).get();
                user.getPlaylist().setPlaylistName(playlist_name);
                userRepo.save(user);
                Playlist playlist1 = user.getPlaylist();
                return playlist1;
            }

        return null;
    }

    @Transactional
    @Override
    public Playlist clearPlaylist(String email) throws UserException {
            if(userRepo.findByEmail(email).isPresent()) {
                User user = userRepo.findByEmail(email).get();
                Playlist ply1 = user.getPlaylist();
                user.getPlaylist().getSongs().clear();
                userRepo.save(user);
                return ply1;

            }else{
                throw new UserException("User is not present");
            }


    }

    @Override
    public List<Song> getSongs(String email) throws UserException {
        if(userRepo.findByEmail(email).isPresent()){
            User user = userRepo.findByEmail(email).get();
            Playlist playlist1 = user.getPlaylist();
            return playlist1.getSongs();
        }else{
            throw new UserException("User does not exist");
        }


    }



}
