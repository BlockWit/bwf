package com.blockwit.bwf.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String login;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String hash;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "account_roles", joinColumns = @JoinColumn(name="account_id"))
    @Enumerated(value = EnumType.STRING)
    @Getter
    @Setter
    private Set<Role> roles;

}
