package com.example.blogservice.backend.repositories;

import com.example.blogservice.backend.models.Post;
import com.example.blogservice.backend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByPublished(boolean published, Pageable pageable);

    List<Post> findAllByAuthorAndPublished(User author, boolean published);
}