package com.spotify11.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int id;

    private String title;
    private String artist;
    private String file_download_uri;
    private String file_uri;
    public Song(String title, String artist, String file_download_uri, String file_uri) {
        this.title = title;
        this.artist = artist;
        this.file_download_uri = file_download_uri;
        this.file_uri = file_uri;
    }



    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}