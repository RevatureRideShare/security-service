-- ENSURE THAT YOU RUN THIS IN THE CORRECT DATABASE AND SCHEMA.
CREATE table security_table 
(
	"user_id" int not null,
	"email" text not null unique,
	"password" text not null,
	"roles" text,
	primary key (user_id))

-- CONVIENIENCE STATEMENT FOR MINOR TABLE ALTERATIONS IF NECESSARY.
--drop table "security";

-- DO NOT USE THE BELOW INSERTS, THEY WILL NOT HAVE THE PASSWORDS ENCODED CORRECTLY.
-- THIS SECTION HAS BEEN KEPT TO SHOW AN EXAMPLE OF THE COLUMNS.
-- VIEW "DbInit.java" IN THE APPLICATION TO EDIT WHAT ROWS ARE INITIALLY ADDED.
--insert into "security" values (1, 'test@test.com', 'pass', 'NULL');
--insert into "security" values (2, 'user@user.com', 'pass', 'USER');
--insert into "security" values (3, 'admin@admin.com', 'pass', 'ADMIN');