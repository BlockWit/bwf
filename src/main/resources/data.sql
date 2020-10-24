INSERT INTO roles VALUES(1, "admin");
INSERT INTO roles VALUES(2, "user");

INSERT INTO permissions VALUES(1, "ref.admin");
INSERT INTO permissions VALUES(2, "ref.user");

INSERT INTO permissions_to_roles VALUES(1, 1);
INSERT INTO permissions_to_roles VALUES(2, 1);
INSERT INTO permissions_to_roles VALUES(1, 2);