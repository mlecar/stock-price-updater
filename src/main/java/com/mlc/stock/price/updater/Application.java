package com.mlc.stock.price.updater;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJms
@EnableScheduling
public class Application {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        SpringApplication.run(Application.class, args);
    }

}
