create table if not exists users  (
  id integer primary key auto_increment,
  first_name varchar(128),
  last_name varchar(128),
  age integer,
  city varchar(128),
  sex char
);


create table if not exists interests (
	id integer primary key auto_increment,
    interest varchar(128) unique
);


create table if not exists users_interests (
    user_id integer,
    interest_id integer
)
