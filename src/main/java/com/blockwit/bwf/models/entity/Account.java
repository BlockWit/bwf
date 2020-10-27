package com.blockwit.bwf.models.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {

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

    @ManyToMany(fetch = FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "roles_to_accounts",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "permissions_to_accounts",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions;

}
