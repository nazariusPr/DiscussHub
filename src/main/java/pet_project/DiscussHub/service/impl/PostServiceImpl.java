package pet_project.DiscussHub.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet_project.DiscussHub.dto.Post.PostRequest;
import pet_project.DiscussHub.dto.Post.PostResponse;
import pet_project.DiscussHub.mapper.PostMapper;
import pet_project.DiscussHub.model.Post;
import pet_project.DiscussHub.model.User;
import pet_project.DiscussHub.repository.PostRepository;
import pet_project.DiscussHub.security.service.impl.JwtService;
import pet_project.DiscussHub.service.PostService;
import pet_project.DiscussHub.service.UserService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static pet_project.DiscussHub.utils.ValidationUtils.validateRequest;

@Service
public class PostServiceImpl implements PostService {
  private final UserService userService;
  private final JwtService jwtService;
  private final PostRepository postRepository;
  private final PostMapper postMapper;

  @Autowired
  public PostServiceImpl(
      UserService userService,
      JwtService jwtService,
      PostRepository postRepository,
      PostMapper postMapper) {
    this.userService = userService;
    this.jwtService = jwtService;
    this.postRepository = postRepository;
    this.postMapper = postMapper;
  }

  @Override
  public Post findPost(UUID id) {
    return this.postRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException("Post with such id = " + id + " was not found!"));
  }

  @Override
  public PostResponse create(PostRequest postRequest, String token) {
    validateRequest(postRequest);

    Post post = this.postMapper.postRequestToPost(postRequest, new Post());
    String email = this.jwtService.extractUsername(token);
    User author = this.userService.findUserByEmail(email);
    post.setAuthor(author);

    this.postRepository.save(post);
    return this.postMapper.postToPostResponse(post);
  }

  @Override
  public List<PostResponse> readAll() {
    List<Post> posts = this.postRepository.findAll();
    return posts.stream().map(postMapper::postToPostResponse).collect(Collectors.toList());
  }

  @Override
  public PostResponse readById(UUID id) {
    return this.postMapper.postToPostResponse(this.findPost(id));
  }

  @Override
  public PostResponse update(UUID id, PostRequest postRequest) {
    validateRequest(postRequest);

    Post post = this.postMapper.postRequestToPost(postRequest, this.findPost(id));
    this.postRepository.save(post);

    return this.postMapper.postToPostResponse(post);
  }

  @Override
  public void delete(UUID id) {
    this.postRepository.delete(this.findPost(id));
  }
}
