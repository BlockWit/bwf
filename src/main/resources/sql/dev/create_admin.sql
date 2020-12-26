INSERT INTO accounts (id, confirm_code, confirmation_status, email, hash, login)
VALUES (3, '673261673130674f53372f4346327342326859494547666c7674674c7548386d433449437773663346497a6e4a45666b495', 'CONFIRMED', 'admin@blockwit.io', '$2a$04$IfHGbXxkAYPYOmQ9Lm.zKeMkkMZcL6jbxqleBAZR8blhryB8/UHv2', 'admin');
INSERT INTO permissions (id, name)
VALUES (1 , 'ADMIN');
INSERT INTO permissions_to_accounts (account_id, permission_id)
VALUES (3, 1);
