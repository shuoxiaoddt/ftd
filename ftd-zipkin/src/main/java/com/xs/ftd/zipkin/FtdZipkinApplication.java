package com.xs.ftd.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin2.server.internal.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer
public class FtdZipkinApplication {

    public static void main(String[] args) {
        SpringApplication.run(FtdZipkinApplication.class, args);
    }

}
