package com.myblog.cms.dto;

public record CreateBlogPostRequest(String slug, String content, String title, String author) {
}