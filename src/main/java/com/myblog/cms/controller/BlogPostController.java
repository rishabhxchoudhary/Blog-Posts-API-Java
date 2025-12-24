package com.myblog.cms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myblog.cms.dto.CreateBlogPostRequest;
import com.myblog.cms.model.BlogPost;
import com.myblog.cms.service.BlogPostService;

@RestController
@RequestMapping("/api")
public class BlogPostController {
    private final BlogPostService blogPostService;

    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @PostMapping("/posts")
    public ResponseEntity<BlogPost> createPost(@RequestBody CreateBlogPostRequest request) {
        BlogPost createdPost = blogPostService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @GetMapping("/posts/{slug}")
    public BlogPost findPostBySlug(@PathVariable String slug) {
        return blogPostService.findBySlug(slug);
    }

}
