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

import java.util.List;


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
    public String addSong(Integer song_id, String email) throws Exception {
        String xyz = "song was never added";
        if (userRepo.findByEmail(email).isPresent()) {
            User user = userRepo.findByEmail(email).get();
            boolean sound_found = false;
            if(songRepo.findById(song_id).isPresent()){
                sound_found = true;
                Song song = songRepo.findById(song_id).get();
                user.getLibrary().addSong(song, email);
            }


            if(sound_found){
                return "THE SONG has been added to " + email;
            }else{
                return "SORRY the sorry is missing";
            }
        }
        return xyz;


    }
    @Transactional
    @Override
    public String removeSong(int song_id, String email) throws SongException, UserException {
    if(userRepo.findByEmail(email).isPresent()) {
        User user1 = userRepo.findByEmail(email).get();
        if(songRepo.findById(song_id).isPresent()){
            Song song = songRepo.findById(song_id).get();
            user1.getPlaylist().getSongs().remove(song);
            userRepo.save(user1);
            return STR."Song title:\{song.getTitle()} removed from playlist";
        }else{
            throw new SongException("Song does not exist");
        }

    }else{
        throw new UserException("User does not exist");
    }

}
    @Transactional
    @Override
    public String readPlaylist(String email) throws Exception {
            if(userRepo.findByEmail(email).isPresent()) {
                User user = userRepo.findByEmail(email).get();
                Playlist playlist1 = user.getPlaylist();
                List<Song> xyz3 = playlist1.getSongs();
                String xyz = "";
                for(Song song : xyz3 ){
                    xyz += song.getTitle() + " " + song.getArtist() + "\n";
                    System.out.println(song.getTitle() + " " + song.getArtist());
                }
                return xyz;
            }else{
                throw new UserException("User is not present");
            }


    }
    @Override
    public String readPlaylistSongs(String email) throws Exception {
        if(userRepo.findByEmail(email).isPresent()) {
            User user = userRepo.findByEmail(email).get();
            Playlist playlist1 = user.getPlaylist();
            List<Song> xyz3 = playlist1.getSongs();
            String xyz = "";
            for(Song song : xyz3 ){
                //xyz = song.getTitle() + " " + song.getArtist() + "\n";
                System.out.println(song.getTitle() + " " + song.getArtist());
            }
            return xyz;
        }else{
            throw new UserException("User is not present");
        }
    }



    @Transactional
    public String renamePlaylist(String email, String playlist_name) throws UserException {
            if(userRepo.findByEmail(email).isPresent()) {
                User user = userRepo.findByEmail(email).get();
                user.getPlaylist().setPlaylistName(playlist_name);
                userRepo.save(user);
                return "Your playlist have been renamed to " + playlist_name;
            }

        return null;
    }

    @Transactional
    @Override
    public String clearPlaylist(String email) throws UserException {
            if(userRepo.findByEmail(email).isPresent()) {
                User user = userRepo.findByEmail(email).get();
                Playlist ply1 = user.getPlaylist();
                user.getPlaylist().getSongs().clear();
                userRepo.save(user);
                return "Playlist name:" + ply1.getPlaylistName() + " has been cleared";
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

    @Override
    public String namePlaylist(String email, String name) throws Exception {
        if(userRepo.findByEmail(email).isPresent()){
            User user = userRepo.findByEmail(email).get();
            Playlist playlist1 = user.getPlaylist();
            playlist1.setPlaylistName(name);
            playlistRepo.save(playlist1);
            return "Your playlist has been renamed to: " + name;
        }else{
            throw new Exception("User cannot be found");
        }
    }


}
