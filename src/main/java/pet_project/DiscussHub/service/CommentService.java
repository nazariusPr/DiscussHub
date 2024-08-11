package pet_project.DiscussHub.service;

import pet_project.DiscussHub.dto.Comment.CommentRequestCreate;
import pet_project.DiscussHub.dto.Comment.CommentRequestUpdate;
import pet_project.DiscussHub.dto.Comment.CommentResponse;
import pet_project.DiscussHub.model.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentService {
  Comment findComment(UUID id);

  CommentResponse create(CommentRequestCreate request);

  List<CommentResponse> readAll();

  CommentResponse readById(UUID id);

  CommentResponse update(UUID id, CommentRequestUpdate request);

  void delete(UUID id);
}
