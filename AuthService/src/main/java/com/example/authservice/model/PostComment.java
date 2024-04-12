package com.example.authservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "post_comment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private PostComment parent;
    @Column(length = 100, nullable = false)
    private String title;
    @Column(nullable = false)
    private Boolean published;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @Column(name = "published_at")
    private Instant publishedAt;
    @Column(columnDefinition = "text")
    private String content;

    @OneToMany(mappedBy = "parent")
    @ToString.Exclude
    private Set<PostComment> postComments;

    public PostComment(Long id) {
        this.id = id;
    }

    public PostComment(Post post, User user, PostComment parent, String title, Boolean published, Instant createdAt, Instant publishedAt, String content) {
        this.post = post;
        this.user = user;
        this.parent = parent;
        this.title = title;
        this.published = published;
        this.createdAt = createdAt;
        this.publishedAt = publishedAt;
        this.content = content;
    }
}
