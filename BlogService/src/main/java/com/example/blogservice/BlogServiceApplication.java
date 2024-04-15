package com.example.blogservice;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.blogservice.backend.models.Post;
import com.example.blogservice.backend.models.User;
import com.example.blogservice.backend.repositories.PostRepository;
import com.example.blogservice.backend.repositories.UserRepository;
import com.thedeanda.lorem.LoremIpsum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.time.Instant;
import java.util.Random;

@EnableRedisHttpSession
@SpringBootApplication
public class BlogServiceApplication {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(BlogServiceApplication.class, args);
    }
}
