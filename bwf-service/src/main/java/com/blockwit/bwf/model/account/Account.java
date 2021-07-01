/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

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

  // TODO: fix to non nullable
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
