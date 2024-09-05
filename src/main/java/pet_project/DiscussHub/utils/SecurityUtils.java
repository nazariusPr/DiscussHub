package pet_project.DiscussHub.utils;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pet_project.DiscussHub.model.Post;
import pet_project.DiscussHub.model.User;
import pet_project.DiscussHub.service.PostService;

@Service("securityUtils")
@AllArgsConstructor
public class SecurityUtils {
  private final PostService postService;

  private UUID currentUserID(Authentication authentication) {
    if (authentication != null
        && authentication.getPrincipal() instanceof UserDetails userDetails) {
      return ((User) userDetails).getId();
    }
    throw new SecurityException("The action is not permitted or user is not authenticated!");
  }

  public boolean hasAccess(UUID userId, Authentication authentication) {
    return userId.equals(this.currentUserID(authentication));
  }

  public boolean isAuthor(UUID postId, Authentication authentication) {
    Post post = this.postService.findPost(postId);
    return this.currentUserID(authentication) == post.getAuthor().getId();
  }
}
