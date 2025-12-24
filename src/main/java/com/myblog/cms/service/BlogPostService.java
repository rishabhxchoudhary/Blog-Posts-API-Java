package com.myblog.cms.service;

import org.springframework.stereotype.Service;

import com.myblog.cms.dto.CreateBlogPostRequest;
import com.myblog.cms.dto.UpdateBlogPostRequest;
import com.myblog.cms.exception.PostNotFoundException;
import com.myblog.cms.model.BlogPost;
import com.myblog.cms.repository.BlogPostRepository;

@Service
public class BlogPostService {
    private final BlogPostRepository repository;

    public BlogPostService(BlogPostRepository repository) {
        this.repository = repository;
    }

    public BlogPost createPost(CreateBlogPostRequest request) {
        BlogPost post = new BlogPost();
        post.setTitle(request.title());
        post.setSlug(request.slug());
        post.setContent(request.content());
        post.setAuthor(request.author());
        return repository.save(post);
    }

    public BlogPost findBySlug(String slug) {
        return repository.findBySlug(slug)
                .orElseThrow(() -> new PostNotFoundException("Post with slug '" + slug + "' not found."));
    }

    public BlogPost updatePost(String slug, UpdateBlogPostRequest request) {
        BlogPost existingBlog = repository.findBySlug(slug)
                .orElseThrow(() -> new PostNotFoundException("Post with slug '" + slug + "' not found."));
        existingBlog.setTitle(request.title());
        existingBlog.setContent(request.content());
        existingBlog.setAuthor(request.author());
        return repository.save(existingBlog);
    }
}
