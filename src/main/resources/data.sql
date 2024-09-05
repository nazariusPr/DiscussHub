-- Insert data into roles table
INSERT INTO roles (id, role)
VALUES ('d1c4f3a5-6e8b-4f3e-b1d6-7d6d5f5d4c6e', 'ROLE_USER'),
       ('e2c4f3a5-7e8b-4f3e-c1d6-8d6d5f5d5c7e', 'ROLE_ADMIN');

-- Insert data into users table
INSERT INTO users (id, nickname, email, password, avatar, bio, created_at, verified)
VALUES ('d1c4f3a5-6e8b-4f3e-b1d6-7d6d5f5d4c6e', 'john_doe', 'john@mail.com',
        '$2a$10$OyzMiySUOATBX3VKXxDPsuf5njk5t1qwgSJSlDh1l4CgKNv3dXkxm', 'avatar_url_1', 'Bio for John Doe',
        '2020-11-16 14:00:00', true),
       ('e2c4f3a5-7e8b-4f3e-c1d6-8d6d5f5d5c7e', 'jane_doe', 'jane@mail.com',
        '$2a$10$OyzMiySUOATBX3VKXxDPsuf5njk5t1qwgSJSlDh1l4CgKNv3dXkxm', 'avatar_url_2', 'Bio for Jane Doe',
        '2020-11-17 14:00:00', true);

-- Insert data into user_roles
INSERT INTO user_roles (user_id, role_id)
VALUES ('d1c4f3a5-6e8b-4f3e-b1d6-7d6d5f5d4c6e', 'd1c4f3a5-6e8b-4f3e-b1d6-7d6d5f5d4c6e'),
       ('e2c4f3a5-7e8b-4f3e-c1d6-8d6d5f5d5c7e', 'e2c4f3a5-7e8b-4f3e-c1d6-8d6d5f5d5c7e'),
       ('d1c4f3a5-6e8b-4f3e-b1d6-7d6d5f5d4c6e', 'e2c4f3a5-7e8b-4f3e-c1d6-8d6d5f5d5c7e');

-- Insert data into posts table
INSERT INTO posts (id, title, content, created_at, updated_at, author_id)
VALUES ('a3c4f3a5-8e8b-4f3e-d1d6-9d6d5f5d6c8e', 'First Post', 'Content of the first post', '2020-11-18 14:00:00', NULL,
        'd1c4f3a5-6e8b-4f3e-b1d6-7d6d5f5d4c6e'),
       ('b4c4f3a5-9e8b-4f3e-e1d6-0d6d5f5d7c9e', 'Second Post', 'Content of the second post', '2020-11-19 14:00:00',
        NULL, 'e2c4f3a5-7e8b-4f3e-c1d6-8d6d5f5d5c7e');

-- Insert data into categories table
INSERT INTO categories (id, name, description)
VALUES ('f1c4f3a5-8e8b-4f3e-b1d6-7d6d5f5d4c6e', 'Technology', 'All about technology'),
       ('a2c4f3a5-9e8b-4f3e-b1d6-7d6d5f5d5c7e', 'Science', 'Discussions about science');

-- Insert data into post_categories table
INSERT INTO post_categories (post_id, category_id)
VALUES ('a3c4f3a5-8e8b-4f3e-d1d6-9d6d5f5d6c8e', 'f1c4f3a5-8e8b-4f3e-b1d6-7d6d5f5d4c6e'),
       ('b4c4f3a5-9e8b-4f3e-e1d6-0d6d5f5d7c9e', 'a2c4f3a5-9e8b-4f3e-b1d6-7d6d5f5d5c7e');

-- Insert data into comments table
INSERT INTO comments (id, content, created_at, updated_at, post_id, commentator_id, parent_comment_id)
VALUES ('c5c4f3a5-0e9b-4f3e-f1d6-1d6d5f5d8c0e', 'Comment on the first post', '2020-11-20 14:00:00', NULL,
        'a3c4f3a5-8e8b-4f3e-d1d6-9d6d5f5d6c8e', 'd1c4f3a5-6e8b-4f3e-b1d6-7d6d5f5d4c6e', NULL),
       ('a3c4f3a5-8e8b-4f3e-d1d6-9d6d5f5d6c8e', 'Comment on the second post', '2020-11-21 14:00:00', NULL,
        'b4c4f3a5-9e8b-4f3e-e1d6-0d6d5f5d7c9e', 'e2c4f3a5-7e8b-4f3e-c1d6-8d6d5f5d5c7e', NULL),
       ('b4c4f3a5-9e8b-4f3e-e1d6-0d6d5f5d7c9e', 'Reply to first comment', '2020-11-22 14:00:00', NULL,
        'a3c4f3a5-8e8b-4f3e-d1d6-9d6d5f5d6c8e', 'e2c4f3a5-7e8b-4f3e-c1d6-8d6d5f5d5c7e',
        'c5c4f3a5-0e9b-4f3e-f1d6-1d6d5f5d8c0e');

-- Insert data into reactions table
INSERT INTO reactions (id, reaction_type, user_id)
VALUES ('b4c4f3a5-9e8b-4f3e-e1d6-0d6d5f5d7c9e', 'LIKE', 'd1c4f3a5-6e8b-4f3e-b1d6-7d6d5f5d4c6e'),
       ('a3c4f3a5-8e8b-4f3e-d1d6-9d6d5f5d6c8e', 'DISLIKE', 'e2c4f3a5-7e8b-4f3e-c1d6-8d6d5f5d5c7e');

-- Inserting sample data for posts_reactions using multiple values
INSERT INTO posts_reactions (post_id, reactions_id)
VALUES ('a3c4f3a5-8e8b-4f3e-d1d6-9d6d5f5d6c8e', 'a3c4f3a5-8e8b-4f3e-d1d6-9d6d5f5d6c8e');

-- Inserting sample data for comments_reactions using multiple values
INSERT INTO comments_reactions (comment_id, reactions_id)
VALUES ('b4c4f3a5-9e8b-4f3e-e1d6-0d6d5f5d7c9e', 'b4c4f3a5-9e8b-4f3e-e1d6-0d6d5f5d7c9e');
