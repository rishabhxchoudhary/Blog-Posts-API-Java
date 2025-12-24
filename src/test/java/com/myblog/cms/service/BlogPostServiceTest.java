package com.myblog.cms.service;

import java.util.Optional;

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
}
