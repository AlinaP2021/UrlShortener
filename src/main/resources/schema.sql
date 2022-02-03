drop table if exists short_links;
drop table if exists users;

create table if not exists short_links (
    id               serial primary key,
    hash             varchar(20) not null unique,
    original_url     varchar,
    start_date       timestamp,
    life_span        int,
    redirects_number int
);

create table if not exists users (
    id       serial primary key,
    username varchar(20) not null,
    password varchar not null
)