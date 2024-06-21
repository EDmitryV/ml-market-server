package com.edmitryv.ml_market_server;

import com.edmitryv.ml_market_server.core.services.MediaService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MlMarketApplication implements CommandLineRunner {
    @Resource
    MediaService mediaService;

    public static void main(String[] args) {
        SpringApplication.run(MlMarketApplication.class, args);
    }

    @Override
    public void run(String... arg) throws Exception {
        mediaService.init();
    }
}
