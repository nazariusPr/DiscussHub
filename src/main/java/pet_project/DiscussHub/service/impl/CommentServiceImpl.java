package pet_project.DiscussHub.service.impl;

import static pet_project.DiscussHub.constant.ErrorConstants.ENTITY_NOT_FOUND_MESSAGE;
import static pet_project.DiscussHub.utils.ValidationUtils.validateRequest;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pet_project.DiscussHub.dto.Comment.CommentRequestCreate;
import pet_project.DiscussHub.dto.Comment.CommentRequestUpdate;
import pet_project.DiscussHub.dto.Comment.CommentResponse;
import pet_project.DiscussHub.mapper.CommentMapper;
import pet_project.DiscussHub.model.Comment;
import pet_project.DiscussHub.repository.CommentRepository;
import pet_project.DiscussHub.service.CommentService;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;

  @Override
  public Comment findComment(UUID id) {
    return this.commentRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_MESSAGE, id)));
  }

  @Override
  public CommentResponse create(CommentRequestCreate request) {
    validateRequest(request);

    Comment comment = this.commentMapper.commentRequestCreateToComment(request);
    if (request.getParentCommentId() != null) {
      comment.setParentComment(this.findComment(request.getParentCommentId()));
    }
    return this.commentMapper.commentToCommentResponse(this.commentRepository.save(comment));
  }

  @Override
  public List<CommentResponse> readAll() {
    return this.commentRepository.findAll().stream()
        .map(this.commentMapper::commentToCommentResponse)
        .collect(Collectors.toList());
  }

  @Override
  public CommentResponse readById(UUID id) {
    return this.commentMapper.commentToCommentResponse(this.findComment(id));
  }

  @Override
  public CommentResponse update(UUID id, CommentRequestUpdate request) {
    validateRequest(request);

    Comment comment =
        this.commentRepository.save(
            this.commentMapper.commentRequestUpdateToComment(request, this.findComment(id)));
    return this.commentMapper.commentToCommentResponse(comment);
  }

  @Override
  public void delete(UUID id) {
    this.commentRepository.delete(this.findComment(id));
  }
}
