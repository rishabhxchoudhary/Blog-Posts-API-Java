package com.myblog.cms.service;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.myblog.cms.dto.CreateBlogPostRequest;
import com.myblog.cms.dto.UpdateBlogPostRequest;
import com.myblog.cms.exception.PostNotFoundException;
import com.myblog.cms.model.BlogPost;
import com.myblog.cms.repository.BlogPostRepository;

@ExtendWith(MockitoExtension.class)
public class BlogPostServiceTest {
    @Mock
    private BlogPostRepository repository;

    @InjectMocks
    private BlogPostService service;

    @Test
    void shouldCreatePostSuccessfully() {
        CreateBlogPostRequest request = new CreateBlogPostRequest(
                "my-test-slug",
                "some cool content",
                "My test title",
                "Rishabh");

        service.createPost(request);

        ArgumentCaptor<BlogPost> blogPostCaptor = ArgumentCaptor.forClass(BlogPost.class);

        verify(repository, times(1)).save(blogPostCaptor.capture());

        BlogPost capturedPost = blogPostCaptor.getValue();
        assertThat(capturedPost.getSlug()).isEqualTo("my-test-slug");
        assertThat(capturedPost.getAuthor()).isEqualTo("Rishabh");
    }

    @Test
    void shouldReturnPostWhenSlugExists() {
        BlogPost myBlogPost = new BlogPost();
        myBlogPost.setSlug("test-slug");
        myBlogPost.setAuthor("Test Author");
        when(repository.findBySlug("test-slug")).thenReturn(Optional.of(myBlogPost));
        BlogPost result = service.findBySlug("test-slug");
        assertThat(result).isEqualTo(myBlogPost);
        assertThat(result.getSlug()).isEqualTo("test-slug");
    }

    @Test
    void shouldThrowPostNotFoundExceptionWhenSlugDoesNotExist() {
        when(repository.findBySlug("non-existent-slug")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> {
            service.findBySlug("non-existent-slug");
        })
                .isInstanceOf(PostNotFoundException.class)
                .hasMessage("Post with slug 'non-existent-slug' not found.");
    }

    @Test
    void shouldUpdatePostSuccessfully() {
        // 1. Define the identifier and the "Before" state of the entity
        final String SLUG = "test-slug";
        BlogPost existingPost = new BlogPost();
        existingPost.setId(UUID.randomUUID());
        existingPost.setSlug(SLUG);
        existingPost.setTitle("Original Title");
        existingPost.setContent("Original Content");
        existingPost.setAuthor("Original Author");

        // 2. Define the user's input (the DTO with the new data)
        UpdateBlogPostRequest updateRequest = new UpdateBlogPostRequest(
                "Updated Title",
                "Updated Content",
                "Updated Author");

        when(repository.findBySlug(SLUG)).thenReturn(Optional.of(existingPost));

        service.updatePost(SLUG, updateRequest);

        ArgumentCaptor<BlogPost> blogPostCaptor = ArgumentCaptor.forClass(BlogPost.class);
        verify(repository).save(blogPostCaptor.capture());
        BlogPost savedPost = blogPostCaptor.getValue();
        assertThat(savedPost.getTitle()).isEqualTo("Updated Title");
        assertThat(savedPost.getContent()).isEqualTo("Updated Content");
        assertThat(savedPost.getId()).isEqualTo(existingPost.getId());
        assertThat(savedPost.getSlug()).isEqualTo(SLUG);
    }

    @Test
    void shouldThrowNotFoundWhenUpdatingNonExistentPost() {
        String slug = "non-existent-slug";
        UpdateBlogPostRequest updateRequest = new UpdateBlogPostRequest("a", "b", "c");
        when(repository.findBySlug(slug)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            service.updatePost(slug, updateRequest);
        }).isInstanceOf(PostNotFoundException.class);
    }
}
