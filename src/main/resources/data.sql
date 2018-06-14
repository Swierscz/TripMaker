INSERT INTO role (role_id, role)
VALUES(1, "ADMIN") ON DUPLICATE KEY UPDATE role="ADMIN";

INSERT INTO locale (locale_id, name)
values(1,"pl_PL") ON DUPLICATE KEY UPDATE name="pl_PL";