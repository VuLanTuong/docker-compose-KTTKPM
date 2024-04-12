package com.example.blogservice.backend.repositories;

import com.example.blogservice.backend.models.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    @Query(value = "FROM PostComment WHERE post.id = :postId AND published = true AND parent is null")
    Page<PostComment> findAllByPostId(@Param("postId") Long postId, Pageable pageable);
}