package com.spotify11.demo.services;


import com.spotify11.demo.entity.Playlist;


import com.spotify11.demo.entity.Song;
import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import jakarta.transaction.Transactional;

import java.util.List;

public interface PlaylistService {

    String getPlaylistName(String email) throws UserException;

    String addSong(Integer song_id, String email) throws Exception;
    List<Song> getSongs(String email) throws UserException;
    String namePlaylist(String email, String name) throws Exception;

    @Transactional
    String removeSong(int song_id, String email) throws SongException, UserException;

    String readPlaylist(String email) throws Exception;
    String readPlaylistSongs(String email) throws Exception;
    String renamePlaylist(String email, String playlist_name) throws UserException;
    String clearPlaylist(String email) throws UserException;


}
