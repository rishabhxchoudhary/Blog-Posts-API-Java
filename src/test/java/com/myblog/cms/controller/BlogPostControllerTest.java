package com.myblog.cms.controller;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myblog.cms.dto.CreateBlogPostRequest;
import com.myblog.cms.exception.PostNotFoundException;
import com.myblog.cms.model.BlogPost;
import com.myblog.cms.service.BlogPostService;

// Add this inside the BlogPostControllerTest class

@WebMvcTest(BlogPostController.class)
public class BlogPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BlogPostService blogPostService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @Test
    void shouldCreatePostAndReturn201() throws Exception {
        CreateBlogPostRequest requestDto = new CreateBlogPostRequest(
                "my-api-slug",
                "some cool content from api",
                "My API Title",
                "Rishabh");

        // This is the object we expect the service to return
        BlogPost serviceResult = new BlogPost();
        serviceResult.setId(UUID.randomUUID());
        serviceResult.setSlug(requestDto.slug());
        serviceResult.setTitle(requestDto.title());

        when(blogPostService.createPost(any(CreateBlogPostRequest.class))).thenReturn(serviceResult);

        mockMvc.perform(
                post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(serviceResult.getId().toString()));

    }

    @Test
    void shouldReturnPostWhenSlugExists() throws Exception {
        BlogPost blog = new BlogPost();
        blog.setTitle("test-title");
        blog.setAuthor("rishabh");
        blog.setContent("test-content");
        blog.setSlug("test-slug");
        blog.setId(UUID.randomUUID());

        when(blogPostService.findBySlug(blog.getSlug())).thenReturn(blog);

        mockMvc.perform(get("/api/posts/{slug}", "test-slug"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(blog.getId().toString()))
                .andExpect(jsonPath("$.title").value(blog.getTitle()))
                .andExpect(jsonPath("$.slug").value(blog.getSlug()));
    }

    @Test
    void shouldReturn404WhenSlugNotFound() throws Exception {
        // Arrange
        String slug = "non-existent-slug";
        when(blogPostService.findBySlug(slug))
                .thenThrow(new PostNotFoundException("Post not found"));

        // Act & Assert
        mockMvc.perform(get("/api/posts/{slug}", slug))
                .andExpect(status().isNotFound());
    }

}
