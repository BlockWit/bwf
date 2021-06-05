package com.blockwit.bwf.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "ip_login_attempts")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LoginAttempts implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(unique = true, nullable = false)
  private String addr;

  @Column
  private Long lastBadAttempt;

  @Column
  private Integer badAttemptsCount;

}
