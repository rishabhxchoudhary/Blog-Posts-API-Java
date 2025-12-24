CREATE TABLE blog_posts (
    id UUID PRIMARY KEY,
    title VARCHAR(255),
    slug VARCHAR(255) NOT NULL UNIQUE,
    content TEXT,
    author VARCHAR(255)
);