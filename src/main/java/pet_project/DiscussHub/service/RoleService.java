package pet_project.DiscussHub.service;

import org.springframework.stereotype.Service;
import pet_project.DiscussHub.model.Role;
import pet_project.DiscussHub.model.enums.RoleType;

@Service
public interface RoleService {
  Role getRole(RoleType roleType);
}
