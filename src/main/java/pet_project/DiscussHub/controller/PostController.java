package pet_project.DiscussHub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pet_project.DiscussHub.dto.Post.PostRequest;
import pet_project.DiscussHub.dto.Post.PostResponse;
import pet_project.DiscussHub.service.PostService;

@Slf4j
@RestController
@RequestMapping("/api/v1/post")
public class PostController {
  private final PostService postService;

  @Autowired
  public PostController(PostService postService) {
    this.postService = postService;
  }

  @PostMapping
  @Operation(
          summary = "Create a new post",
          description = "Creates a new post with the provided request details.")
  @ApiResponse(responseCode = "201", description = "Post created successfully")
  @ApiResponse(responseCode = "400", description = "Invalid request due to validation errors")
  public ResponseEntity<PostResponse> create(
          @RequestHeader(name = "Authorization") String token,
          @Validated @RequestBody PostRequest request,
          BindingResult result) {
    if (result.hasErrors()) {
      throw new ValidationException("Invalid request to create new post");
    }

    PostResponse response = this.postService.create(request, token);

    log.info("** /Creating new post");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @Secured("ROLE_ADMIN")
  @GetMapping("/all")
  @Operation(
          summary = "Get all posts",
          description = "Retrieves a list of all posts. Only accessible by users with the ADMIN role.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of posts")
  public ResponseEntity<List<PostResponse>> getAll() {
    List<PostResponse> response = this.postService.readAll();

    log.info("** /Getting all posts");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  @Operation(
          summary = "Get a post by ID",
          description = "Retrieves a post based on the provided ID.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved the post")
  @ApiResponse(responseCode = "404", description = "Post not found")
  public ResponseEntity<PostResponse> getById(
          @Parameter(description = "ID of the post to be retrieved") @PathVariable(name = "id") UUID id) {
    PostResponse response = this.postService.readById(id);

    log.info("**/ Getting post by id = " + id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PreAuthorize("@securityUtils.isAuthor(#id, authentication) or hasRole('ROLE_ADMIN')")
  @PutMapping("/{id}")
  @Operation(
          summary = "Update a post",
          description = "Updates an existing post based on the provided ID and request details.")
  @ApiResponse(responseCode = "200", description = "Successfully updated the post")
  @ApiResponse(responseCode = "400", description = "Invalid request due to validation errors")
  @ApiResponse(responseCode = "404", description = "Post not found")
  public ResponseEntity<PostResponse> update(
          @Parameter(description = "ID of the post to be updated") @PathVariable(name = "id") UUID id,
          @Validated @RequestBody PostRequest request,
          BindingResult result) {
    if (result.hasErrors()) {
      throw new ValidationException("Invalid request to update post");
    }

    PostResponse response = this.postService.update(id, request);

    log.info("**/ Updating post by id = " + id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PreAuthorize("@securityUtils.isAuthor(#id, authentication) or hasRole('ROLE_ADMIN')")
  @DeleteMapping("/{id}")
  @Operation(
          summary = "Delete a post",
          description = "Deletes a post based on the provided ID.")
  @ApiResponse(responseCode = "204", description = "Successfully deleted the post")
  @ApiResponse(responseCode = "404", description = "Post not found")
  public ResponseEntity<Void> delete(
          @Parameter(description = "ID of the post to be deleted") @PathVariable(name = "id") UUID id) {
    this.postService.delete(id);

    log.info("**/ Deleting post by id = " + id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
