package pet_project.DiscussHub.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pet_project.DiscussHub.model.Role;
import pet_project.DiscussHub.model.enums.RoleType;
import pet_project.DiscussHub.repository.RoleRepository;
import pet_project.DiscussHub.service.RoleService;

import static pet_project.DiscussHub.constant.ErrorConstants.ENTITY_NOT_FOUND_MESSAGE;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
  private final RoleRepository roleRepository;

  @Override
  public Role getRole(RoleType roleType) {
    return this.roleRepository
        .findByRole(roleType)
        .orElseThrow(
            () -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_MESSAGE, roleType)));
  }
}
