package pet_project.DiscussHub.service.impl;

import static pet_project.DiscussHub.utils.ValidationUtils.validateRequest;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pet_project.DiscussHub.dto.Authentication.RegisterRequest;
import pet_project.DiscussHub.dto.User.UserPageDto;
import pet_project.DiscussHub.dto.User.UserRequest;
import pet_project.DiscussHub.dto.User.UserResponse;
import pet_project.DiscussHub.mapper.AuthenticationMapper;
import pet_project.DiscussHub.mapper.UserMapper;
import pet_project.DiscussHub.model.User;
import pet_project.DiscussHub.model.enums.Status;
import pet_project.DiscussHub.repository.UserRepository;
import pet_project.DiscussHub.service.UserService;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final AuthenticationMapper authMapper;

  @Autowired
  public UserServiceImpl(
      UserRepository userRepository, UserMapper userMapper, AuthenticationMapper authMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.authMapper = authMapper;
  }

  private User findUser(UUID id) {
    return this.userRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User with id = " + id + " was not found!"));
  }

  @Override
  public User findUserByEmail(String email) {
    return this.userRepository
        .findByEmail(email)
        .orElseThrow(
            () -> new EntityNotFoundException("User with email = " + email + " was not found!"));
  }

  @Override
  public UserResponse create(RegisterRequest request) {
    validateRequest(request);

    User user = this.userRepository.save(this.authMapper.registerRequestToUser(request));
    return this.userMapper.userToUserResponse(user);
  }

  @Override
  public List<UserResponse> readAll() {
    return this.userRepository.findAll().stream()
        .map(this.userMapper::userToUserResponse)
        .collect(Collectors.toList());
  }

  @Override
  public UserPageDto readPage(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<User> users = this.userRepository.findAll(pageable);

    return new UserPageDto(
        users.getContent().stream()
            .map(this.userMapper::userToUserResponse)
            .collect(Collectors.toList()),
        users.getNumber(),
        users.getTotalElements(),
        users.getTotalPages());
  }

  @Override
  public UserResponse readById(UUID id) {
    return this.userMapper.userToUserResponse(this.findUser(id));
  }

  @Override
  public UserResponse readByEmail(String email) {
    User user = this.findUserByEmail(email);
    return this.userMapper.userToUserResponse(user);
  }

  @Override
  public UserResponse update(UUID id, UserRequest request) {
    validateRequest(request);

    User user =
        this.userRepository.save(this.userMapper.userRequestToUser(request, this.findUser(id)));
    return this.userMapper.userToUserResponse(user);
  }

  @Override
  public UserResponse update(String email, UserRequest request) {
    validateRequest(request);

    User user =
        this.userRepository.save(
            this.userMapper.userRequestToUser(request, this.findUserByEmail(email)));
    return this.userMapper.userToUserResponse(user);
  }

  @Override
  public void delete(UUID id) {
    this.userRepository.delete(this.findUser(id));
  }

  @Override
  public void updateUserStatus(String email, Status status) {
    User user = this.findUserByEmail(email);
    user.setStatus(status);

    this.userRepository.save(user);
  }
}
