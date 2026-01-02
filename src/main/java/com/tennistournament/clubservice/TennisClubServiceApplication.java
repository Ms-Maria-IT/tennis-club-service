package com.tennistournament.clubservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
public class TennisClubServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TennisClubServiceApplication.class, args);
    }
}
