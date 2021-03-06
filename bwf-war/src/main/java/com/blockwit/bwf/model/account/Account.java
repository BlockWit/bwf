package com.blockwit.bwf.model.account;

import com.blockwit.bwf.model.ConfirmationStatus;
import com.blockwit.bwf.model.Permission;
import com.blockwit.bwf.model.Role;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "accounts")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(length = 100, nullable = false, unique = true)
  private String login;

  @Column(length = 100, nullable = false, unique = true)
  private String email;

  @Column(length = 60)
  private String hash;

  @Column(length = 99)
  private String confirmCode;

  @Column(length = 99)
  private String passwordRecoveryCode;

  @Column()
  private Long passwordRecoveryTimestamp;

  @Enumerated(value = EnumType.STRING)
  private ConfirmationStatus confirmationStatus;

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinTable(
      name = "roles_to_accounts",
      joinColumns = @JoinColumn(name = "account_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<Role> roles;

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
  @JoinTable(
      name = "permissions_to_accounts",
      joinColumns = @JoinColumn(name = "account_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id")
  )
  private Set<Permission> permissions;

  public boolean isApproved() {
    return getConfirmationStatus() == ConfirmationStatus.CONFIRMED;
  }

  public String getRolesHR() {
    return getRoles().stream().map(t -> t.getName()).collect(Collectors.joining(","));
  }

}
