/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accounts`
(
    `id`                          bigint(20)   NOT NULL,
    `confirm_code`                varchar(99)  DEFAULT NULL,
    `confirmation_status`         varchar(255) DEFAULT NULL,
    `email`                       varchar(100) NOT NULL,
    `hash`                        varchar(60)  DEFAULT NULL,
    `login`                       varchar(100) NOT NULL,
    `password_recovery_code`      varchar(99)  DEFAULT NULL,
    `password_recovery_timestamp` bigint(20)   DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_n7ihswpy07ci568w34q0oi8he` (`email`),
    UNIQUE KEY `UK_cc2c9baeppipgy2rjeccwcqs0` (`login`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `accounts`
VALUES (1, NULL, 'CONFIRMED', 'cromlehg@gmail.com', '$2a$10$nu.CoZJLntrHNJqiJCrdy.TUQuJUhB/TsFYNo.crTdV8rL7W28xsW',
        'cromlehg', NULL, NULL);
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles`
(
    `id`   bigint(20)   NOT NULL,
    `name` varchar(150) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `roles`
VALUES (1, 'ADMIN');
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permissions`
(
    `id`   bigint(20)   NOT NULL,
    `name` varchar(150) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `permissions`
VALUES (1, 'ADMIN');
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permissions_to_accounts`
(
    `account_id`    bigint(20) NOT NULL,
    `permission_id` bigint(20) NOT NULL,
    PRIMARY KEY (`account_id`, `permission_id`),
    KEY `FKjka8xq6bnvf2bc17lm6sj7cmu` (`permission_id`),
    CONSTRAINT `FKjka8xq6bnvf2bc17lm6sj7cmu` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`),
    CONSTRAINT `FKowtqth8jobkwxe6ck5jbwrotj` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permissions_to_roles`
(
    `role_id`       bigint(20) NOT NULL,
    `permission_id` bigint(20) NOT NULL,
    PRIMARY KEY (`role_id`, `permission_id`),
    KEY `FKmmnsfshu4nthpn7s07e34254b` (`permission_id`),
    CONSTRAINT `FKmmnsfshu4nthpn7s07e34254b` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`),
    CONSTRAINT `FKtbvvp061ucqlv8g0mmgfc99i7` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `permissions_to_roles`
VALUES (1, 1);
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles_to_accounts`
(
    `account_id` bigint(20) NOT NULL,
    `role_id`    bigint(20) NOT NULL,
    PRIMARY KEY (`account_id`, `role_id`),
    KEY `FK9gevgi43t2phwbivv8sm1ife5` (`role_id`),
    CONSTRAINT `FK915pwysiccqauurf41iqanp6o` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
    CONSTRAINT `FK9gevgi43t2phwbivv8sm1ife5` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `roles_to_accounts`
VALUES (1, 1);