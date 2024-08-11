package pet_project.DiscussHub.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet_project.DiscussHub.model.Role;
import pet_project.DiscussHub.model.enums.RoleType;
import pet_project.DiscussHub.repository.RoleRepository;
import pet_project.DiscussHub.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
  private final RoleRepository roleRepository;

  @Autowired
  public RoleServiceImpl(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Override
  public Role getRole(RoleType roleType) {
    return this.roleRepository
        .findByRole(roleType)
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    "Role with type = " + roleType.name() + " was not found"));
  }
}
