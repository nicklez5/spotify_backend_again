package com.spotify11.demo;



import com.spotify11.demo.configuration.ApplicationConfiguration;
import com.spotify11.demo.configuration.JwtAuthenticationFilter;
import com.spotify11.demo.configuration.SecurityConfiguration;
import com.spotify11.demo.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.net.Socket;


@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class,
})

public class DemoApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(DemoApplication.class, args);

	}



}

