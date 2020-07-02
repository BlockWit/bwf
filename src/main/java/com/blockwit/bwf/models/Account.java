package com.blockwit.bwf.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
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

    public Account(Long id, String login, String email, String hash) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.hash = hash;
    }
}
