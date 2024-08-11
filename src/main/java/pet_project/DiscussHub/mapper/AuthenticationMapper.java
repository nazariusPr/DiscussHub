package pet_project.DiscussHub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import pet_project.DiscussHub.dto.Authentication.RegisterRequest;
import pet_project.DiscussHub.model.User;

@Mapper(componentModel = "spring")
public abstract class AuthenticationMapper {
  protected PasswordEncoder passwordEncoder;

  @Autowired
  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Mapping(target = "password", source = "password", qualifiedByName = "mapPassword")
  public abstract User registerRequestToUser(RegisterRequest userRequest);

  @Named("mapPassword")
  protected String mapPassword(String password) {
    return this.passwordEncoder.encode(password);
  }
}
