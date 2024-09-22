package com.spotify11.demo.servlets;

import com.spotify11.demo.entity.Song;
import com.spotify11.demo.utilites.Functions;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
@WebServlet(name = "FileUploadServlet", urlPatterns = { "/upload_me"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 100
)
public class DownloadServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp, MultipartFile file, String title, String artist) throws IOException {
       File file123 = Functions.multipartFile(file, StringUtils.cleanPath(file.getOriginalFilename()));
       try{
           FileInputStream inputStream = new FileInputStream(file123);
           int i = inputStream.read();
           while(i != -1){
               i = inputStream.read();
           }
           inputStream.close();
       }catch(IOException e){
           e.printStackTrace();
       }
       Song song123 = new Song();
       song123.setTitle(title);
       song123.setArtist(artist);
    }
    protected HttpServletRequest doGet(HttpServletRequest req, HttpServletResponse resp, MultipartFile file) throws ServletException, IOException {
        String id = req.getParameter("id");
        resp.setContentType("audio/mpeg");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        resp.setHeader("Content-Length", String.valueOf(file.getSize()));
        resp.setHeader("Content-Type", "audio/mpeg");
        resp.setHeader("Content-Transfer-Encoding", "binary");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Expires", "0");
        try(OutputStream out = resp.getOutputStream()){
            out.write(file.getBytes());
        }catch(IOException e){
            e.printStackTrace();
        }
        return req;


    }
}
