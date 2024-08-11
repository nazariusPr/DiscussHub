package pet_project.DiscussHub.controller;

import jakarta.validation.ValidationException;
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

import java.util.List;
import java.util.UUID;

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
  public ResponseEntity<List<PostResponse>> getAll() {
    List<PostResponse> response = this.postService.readAll();

    log.info("** /Getting all posts");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostResponse> getById(@PathVariable(name = "id") UUID id) {
    PostResponse response = this.postService.readById(id);

    log.info("**/ Getting post by id = " + id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PreAuthorize("@securityUtils.isAuthor(#id, authentication) or hasRole('ROLE_ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<PostResponse> update(
      @PathVariable(name = "id") UUID id,
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
  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) {
    this.postService.delete(id);

    log.info("**/ Deleting post by id = " + id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
