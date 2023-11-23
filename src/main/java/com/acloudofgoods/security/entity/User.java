package com.acloudofgoods.security.entity;

import jakarta.persistence.*;

import java.util.*;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "auth_user")
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Long  id;
  private String firstname;
  private String lastname;
  @Column(unique = true)
  private String email;
  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "users_authorities",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "authority_id"))
  private Set<Role> roles;
  @OneToMany(mappedBy = "user")
  private List<Token> tokens;

  public Collection<? extends GrantedAuthority> getAuthorities() {  // authorities AND roles
    var authorities = this.roles;
    List<GrantedAuthority> roleList = new ArrayList<>();
    for (var a: authorities) {
        roleList.add(new SimpleGrantedAuthority("ROLE_" + a.getRoleName()));
    }
    return roleList;
  }


  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
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

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", firstname='" + firstname + '\'' +
            ", lastname='" + lastname + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            '}';
  }
}
