package pet_project.DiscussHub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pet_project.DiscussHub.dto.User.UserRequest;
import pet_project.DiscussHub.dto.User.UserResponse;
import pet_project.DiscussHub.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserResponse userToUserResponse(User user);

  User userRequestToUser(UserRequest userRequest, @MappingTarget User user);
}
