package com.ticktickdoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Service {

    public static void main(String[] args) {
        SpringApplication.run(Service.class, args);
    }

}
