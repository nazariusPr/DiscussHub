package pet_project.DiscussHub.service;

import pet_project.DiscussHub.dto.Post.PostRequest;
import pet_project.DiscussHub.dto.Post.PostResponse;
import pet_project.DiscussHub.model.Post;

import java.util.List;
import java.util.UUID;

public interface PostService {
  Post findPost(UUID id);

  PostResponse create(PostRequest postRequest, String token);

  List<PostResponse> readAll();

  PostResponse readById(UUID id);

  PostResponse update(UUID id, PostRequest postRequest);

  void delete(UUID id);
}
