package pet_project.DiscussHub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank
  @Column(name = "nickname", unique = true)
  private String nickname;

  @NotBlank
  @Column(name = "email", unique = true)
  private String email;

  @Pattern(
      regexp = "[A-Za-z\\d]{6,}",
      message = "Must be minimum 6 symbols long, using digits and latin letters")
  @NotBlank
  @Column(name = "password")
  private String password;

  @Column(name = "avatar")
  private String avatar;

  @Size(max = 255)
  @Column(name = "bio")
  private String bio;

  @NotNull
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "verified")
  private boolean verified;

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
  private List<Post> posts;

  @OneToMany(mappedBy = "commentator", cascade = CascadeType.ALL)
  private List<Comment> comments;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles;

  @PrePersist
  private void onCreate() {
    this.createdAt = LocalDateTime.now();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
        .collect(Collectors.toList());
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
