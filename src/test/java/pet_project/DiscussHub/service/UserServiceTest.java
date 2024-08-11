package pet_project.DiscussHub.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pet_project.DiscussHub.dto.Authentication.RegisterRequest;
import pet_project.DiscussHub.dto.User.UserRequest;
import pet_project.DiscussHub.dto.User.UserResponse;
import pet_project.DiscussHub.exception.NullDtoReferenceException;
import pet_project.DiscussHub.model.Post;
import pet_project.DiscussHub.model.Role;
import pet_project.DiscussHub.model.User;
import pet_project.DiscussHub.model.enums.RoleType;
import pet_project.DiscussHub.repository.UserRepository;
import pet_project.DiscussHub.service.impl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void UserService_Create_ReturnUserResponse(){
        String nickname = "testuser";
        String email = "test@example.com";
        String password = "password123";

        User user = new User();
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPassword(password);

        RegisterRequest request = new RegisterRequest(
                nickname,
                email,
                password
        );

        when(userRepository.save(user)).thenReturn(user);

        UserResponse response = this.userService.create(request);

        Assertions.assertEquals(user.getId(), response.getId());
        Assertions.assertEquals(user.getNickname(), response.getNickname());
        Assertions.assertEquals(user.getEmail(), response.getEmail());
        Assertions.assertEquals(user.getAvatar(), response.getAvatar());
        Assertions.assertEquals(user.getBio(), response.getBio());
    }

    @Test
    public void UserService_Create_ThrowNullDtoReferenceException(){
        String nickname = "testuser";
        String email = "test@example.com";
        String password = "password123";

        User user = new User();
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPassword(password);

        when(userRepository.save(user)).thenReturn(user);

        Assertions.assertThrows(NullDtoReferenceException.class, () -> this.userService.create(null));

    }

    @Test
    public void UserService_ReadById_ReturnUserResponse(){
        UUID userId = UUID.randomUUID();
        String nickname = "testuser";
        String email = "test@example.com";
        String password = "password123";
        String avatar = "avatar.png";
        String bio = "This is a bio";
        LocalDateTime createdAt = LocalDateTime.now();
        List<Post> posts = new ArrayList<>();
        Set<Role> roles = new HashSet<>();

        User user = new User(userId, nickname, email, password, avatar, bio, createdAt, posts, roles);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponse response = this.userService.readById(userId);

        Assertions.assertEquals(userId, response.getId());
        Assertions.assertEquals(nickname, response.getNickname());
        Assertions.assertEquals(email, response.getEmail());
        Assertions.assertEquals(avatar, response.getAvatar());
        Assertions.assertEquals(bio, response.getBio());
    }

    @Test
    public void UserService_ReadById_ThrowEntityNotFoundException(){
        UUID userId = UUID.randomUUID();
        when(this.userRepository.findById(userId))
                .thenThrow(new EntityNotFoundException("User with id = " + userId + " was not found!"));

        Assertions.assertThrows(EntityNotFoundException.class, () -> this.userService.readById(userId));
    }

    @Test
    public void UserService_FindByEmail_ReturnUserResponse(){
        String nickname = "testuser";
        String email = "test@example.com";
        String password = "password123";

        User user = new User();
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPassword(password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserResponse response = this.userService.readByEmail(email);

        Assertions.assertEquals(user.getId(), response.getId());
        Assertions.assertEquals(user.getNickname(), response.getNickname());
        Assertions.assertEquals(user.getEmail(), response.getEmail());
        Assertions.assertEquals(user.getAvatar(), response.getAvatar());
        Assertions.assertEquals(user.getBio(), response.getBio());
    }

    @Test
    public void UserService_FindByEmail_ThrowEntityNotFoundException(){
        String email = "invalidEmail";
        when(this.userRepository.findByEmail(email))
                .thenThrow(new EntityNotFoundException("User with email = " + email + " was not found!"));

        Assertions.assertThrows(EntityNotFoundException.class, () -> this.userService.readByEmail(email));
    }

}
