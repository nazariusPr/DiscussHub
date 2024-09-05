package pet_project.DiscussHub.mapper;

import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import pet_project.DiscussHub.dto.Comment.CommentRequestCreate;
import pet_project.DiscussHub.dto.Comment.CommentRequestUpdate;
import pet_project.DiscussHub.dto.Comment.CommentResponse;
import pet_project.DiscussHub.model.Comment;
import pet_project.DiscussHub.model.Post;
import pet_project.DiscussHub.service.PostService;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {
  protected PostService postService;

  @Autowired
  public void setPostService(PostService postService) {
    this.postService = postService;
  }

  public abstract CommentResponse commentToCommentResponse(Comment comment);

  @Mapping(target = "post", source = "parentCommentId", qualifiedByName = "mapPost")
  public abstract Comment commentRequestCreateToComment(CommentRequestCreate commentRequestCreate);

  public abstract Comment commentRequestUpdateToComment(
      CommentRequestUpdate commentRequestUpdate, @MappingTarget Comment comment);

  @Named("mapPost")
  protected Post mapPost(UUID id) {
    return this.postService.findPost(id);
  }
}
