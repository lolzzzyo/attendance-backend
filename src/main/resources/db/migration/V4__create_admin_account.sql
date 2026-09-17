INSERT INTO account (
    first_name,
    nickname,
    last_name,
    email,
    password_hash,
    role,
    active
)
VALUES (
           'Admin',
           NULL,
           'Account',
           'admin@admin.com',
           '$2a$10$iac.YXOKv5OpWcT.zh0wEuK79.xXRFOn92Z/CstntF2K69.ybACRS',
           'ADMIN',
           TRUE
       );