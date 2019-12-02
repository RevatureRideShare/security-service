-- ENSURE THAT YOU RUN THIS IN THE CORRECT DATABASE AND SCHEMA.
CREATE TABLE SECURITY (user_id identity primary key, email varchar(50) not null unique, password varchar(50) not null, roles text)

-- CONVIENIENCE STATEMENT FOR MINOR TABLE ALTERATIONS IF NECESSARY.
--drop table "security";

-- DO NOT USE THE BELOW INSERTS, THEY WILL NOT HAVE THE PASSWORDS ENCODED CORRECTLY.
-- THIS SECTION HAS BEEN KEPT TO SHOW AN EXAMPLE OF THE COLUMNS.
-- VIEW "DbInit.java" IN THE APPLICATION TO EDIT WHAT ROWS ARE INITIALLY ADDED.
--insert into "security" values (1, 'test@test.com', 'pass', 'NULL');
--insert into "security" values (2, 'user@user.com', 'pass', 'USER');
--insert into "security" values (3, 'admin@admin.com', 'pass', 'ADMIN');