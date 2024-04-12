package com.example.blogservice.controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.blogservice.backend.models.Post;
import com.example.blogservice.backend.models.PostComment;
import com.example.blogservice.backend.models.User;
import com.example.blogservice.backend.repositories.PostCommentRepository;
import com.example.blogservice.backend.repositories.PostRepository;
import com.example.blogservice.backend.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RestController
@RequestMapping("/blog")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final UserRepository userRepository;

    public AuthController(PostRepository postRepository, PostCommentRepository postCommentRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(value = {"/", "/index", "/posts"})
    public ModelAndView index(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size, HttpSession session) {
        int pageNum = page.orElse(1);
        int sizeNum = size.orElse(10);

        PageRequest pageable = PageRequest.of(pageNum - 1, sizeNum, Sort.by("publishedAt"));

        Page<Post> posts = postRepository.findAllByPublished(true, pageable);

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("user", session.getAttribute("user"));

        modelAndView.addObject("posts", posts);
        modelAndView.addObject("pages", IntStream.rangeClosed(1, posts.getTotalPages()).boxed().collect(Collectors.toList()));

        modelAndView.setViewName("index");

        return modelAndView;
    }



    @GetMapping(value = {"/posts/{id}"})
    public ModelAndView postDetail(@PathVariable("id") String id, @RequestParam("page") Optional<Integer> page, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        Integer pageNum = page.orElse(1);

        try {
            Long idLong = Long.parseLong(id);
            Optional<Post> post = postRepository.findById(idLong);


            if (post.isPresent()) {
                PageRequest pageRequest = PageRequest.of(0, 5 * pageNum, Sort.by("createdAt").descending());

                Page<PostComment> comments = postCommentRepository.findAllByPostId(idLong, pageRequest);
                PostComment postComment = new PostComment();
                PostComment parenPostComment = new PostComment();

                modelAndView.addObject("post", post.get());
                modelAndView.addObject("comments", comments);
                modelAndView.addObject("postComment", postComment);
                modelAndView.addObject("parenPostComment", parenPostComment);
                modelAndView.addObject("user", session.getAttribute("user"));
                modelAndView.addObject("pageNext", comments.getSize() / 5 + 1);

                modelAndView.setViewName("posts/detail");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            modelAndView.setViewName("notFound");
        }

        return modelAndView;
    }

    @GetMapping("/posts/add")
    public String addPost(HttpSession session, Model model) {
        Object object = session.getAttribute("user");

        if (object == null) {
            return "redirect:/login";
        }

        User user = (User) object;

        List<Post> posts = postRepository.findAllByAuthorAndPublished(user, true);

        Post post = new Post();
        Post parentPost = new Post();
        post.setParent(parentPost);

        model.addAttribute("post", post);
        model.addAttribute("parentPost", parentPost);
        model.addAttribute("posts", posts);
        model.addAttribute("user", user);

        return "posts/add";
    }

    @PostMapping("/posts/add")
    public String addPost(@ModelAttribute("post") Post post, @ModelAttribute("parentPost") Post parentPost, HttpSession session) {
        Object object = session.getAttribute("user");

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        if (parentPost.getId() != null) {
            Optional<Post> parentPostOptional = postRepository.findById(parentPost.getId());

            parentPostOptional.ifPresent(post::setParent);
        }

        User user = (User) object;
        post.setId(null);
        post.setAuthor(user);
        post.setPublished(true);
        post.setCreatedAt(Instant.now());
        post.setPublishedAt(Instant.now());

        if (parentPost.getId() != null) {
            Optional<Post> parentPostOptional = postRepository.findById(parentPost.getId());

            parentPostOptional.ifPresent(post::setParent);
        }

        postRepository.save(post);

        return "redirect:/index";
    }

    @PostMapping("/posts/{id}/comment")
    public ModelAndView addComment(@ModelAttribute("postComment") PostComment postComment, @ModelAttribute("parent-comment") String parentCommentId, @PathVariable("id") Long postId, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        Object object = session.getAttribute("user");

        if (object == null) {
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        postComment.setId(null);
        postComment.setPost(new Post(postId));
        postComment.setPublished(true);
        postComment.setCreatedAt(Instant.now());
        postComment.setUser((User) object);
        if (postComment.getContent() != null && postComment.getContent().isEmpty())
            postComment.setContent(null);

        if (!parentCommentId.isEmpty()) {
            long parentCommentIdLong = Long.parseLong(parentCommentId);

            PostComment parentPostComment = new PostComment(parentCommentIdLong);

            postComment.setParent(parentPostComment);
        }

        postCommentRepository.save(postComment);

        modelAndView.setViewName("redirect:/posts/" + postId);
        return modelAndView;
    }
}