package pet_project.DiscussHub.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pet_project.DiscussHub.model.Role;
import pet_project.DiscussHub.model.User;
import pet_project.DiscussHub.model.enums.RoleType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

@SpringBootTest
@Transactional
public class UserRepositoryTest {
    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Test
    void UserRepository_FindByEmail_ReturnOptionalUser(){
        String email = "john@mail.com";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        User expected = new User(UUID.fromString("d1c4f3a5-6e8b-4f3e-b1d6-7d6d5f5d4c6e"),
                "john_doe", "john@mail.com",
                "$2a$10$OyzMiySUOATBX3VKXxDPsuf5njk5t1qwgSJSlDh1l4CgKNv3dXkxm",
                "avatar_url_1", "Bio for John Doe",  LocalDateTime.parse("2020-11-16 14:00:00", formatter),
                new ArrayList<>(), new HashSet<>());

        User actual = this.userRepository.findByEmail(email).get();

        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getUsername(), actual.getUsername());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertEquals(expected.getAvatar(), actual.getAvatar());
        Assertions.assertEquals(expected.getBio(), actual.getBio());
        Assertions.assertEquals(expected.getCreatedAt(), actual.getCreatedAt());

    }
}
