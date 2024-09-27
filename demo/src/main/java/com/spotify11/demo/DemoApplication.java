package com.spotify11.demo;



import com.spotify11.demo.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;


@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class,
})

public class DemoApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(DemoApplication.class, args);

	}



}

