package com.myblog.cms.repository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.myblog.cms.model.BlogPost;


@DataJpaTest()
public class BlogPostRepositoryTest {
    @Autowired
    private BlogPostRepository repository;

    @Test
    void shouldFindPostBySlug() {
        BlogPost post = new BlogPost();
        post.setTitle("Hello World");
        post.setSlug("hello-world-slug");
        post.setContent("This is my first post content.");
        post.setAuthor("Rishabh");

        repository.save(post);
        Optional<BlogPost> result = repository.findBySlug("hello-world-slug");

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Hello World");
    }

    @Test
    void shouldReturnEmptyWhenSlugNotFound() {
        Optional<BlogPost> result = repository.findBySlug("non-existent-slug");
        assertThat(result).isEmpty();
    }
}
