 create database bankdb;
 use bankdb;
 create table users (
 id int auto_increment primary key ,
 name varchar(50),
 email varchar(50) unique,
 password varchar(50) not null
 );
 
 create table accounts(
 acc_id int auto_increment primary key,
 user_id int,
 balance double default 0,
constraint ids foreign key (user_id) references users(id)
 );
 
select * from users;
select * from accounts;