create sequence hibernate_sequence;

create table actors
(
    id        varchar(255) not null
        primary key,
    full_name varchar(255) not null
        constraint uk_a2pj813lkadxpcuc09to30r00
            unique
);

create table countries
(
    id   varchar(255) not null
        primary key,
    name varchar(255) not null
        constraint uk_1pyiwrqimi3hnl3vtgsypj5r
            unique
);

create table genres
(
    id   varchar(255) not null
        primary key,
    name varchar(255) not null
        constraint uk_pe1a9woik1k97l87cieguyhh4
            unique
);

create table media
(
    id      varchar(255) not null
        primary key,
    poster  text,
    trailer text
);

create table movie_details
(
    id              varchar(255) not null
        primary key,
    description     varchar(255),
    director        varchar(255),
    duration        integer
        constraint movie_details_duration_check
            check (duration >= 1),
    production_year integer
        constraint movie_details_production_year_check
            check ((production_year <= 2023) AND (production_year >= 1850)),
    country_id      varchar(255)
        constraint fkqq43k1gd79fml19b3ss7fqunv
            references countries,
    media_id        varchar(255)
        constraint fkcpny4tmi2ufvcnggwd0jgqdlt
            references media
);

create table movie_details_actors
(
    movie_details_id varchar(255) not null
        constraint fk3edtpdtubjhbag5ess3hjdw0o
            references movie_details,
    actor_id         varchar(255) not null
        constraint fkcxnm90yk3pc5qmndc8sj6mc8d
            references actors,
    primary key (movie_details_id, actor_id)
);

create table movie_details_genres
(
    movie_details_id varchar(255) not null
        constraint fkfxt4hlsrjve8bmwcki0p2dokc
            references movie_details,
    genres_id        varchar(255) not null
        constraint fk5ayw8r0ji0uhu9oxouokr1vxn
            references genres,
    primary key (movie_details_id, genres_id)
);

create table roles
(
    id   varchar(255) not null
        primary key,
    name varchar(255) not null
        constraint uk_ofx66keruapi6vyqpv6f2or37
            unique
);

create table sessions
(
    id               varchar(255) not null
        primary key,
    name             varchar(255),
    start_time       timestamp,
    enabled  boolean      not null,
    movie_details_id varchar(255)
        constraint fkm9nl4yfdexdjq1fy6jmmpvi8k
            references movie_details
);

create table users
(
    id       varchar(255) not null
        primary key,
    email    varchar(255)
        constraint uk_6dotkott2kjsp8vw4d0m25fb7
            unique,
    enabled  boolean      not null,
    login    varchar(255) not null
        constraint uk_ow0gan20590jrb00upg3va2fn
            unique,
    password varchar(255) not null
);

create table tickets
(
    id         varchar(255) not null
        primary key,
    bought_at  timestamp,
    price      integer,
    row        integer,
    seat       integer,
    type       varchar(255),
    session_id varchar(255)
        constraint fk6yhwfajgdoqa8kq4gnuimtkpp
            references sessions,
    user_id    varchar(255)
        constraint fk4eqsebpimnjen0q46ja6fl2hl
            references users
);

create table users_roles
(
    user_id varchar(255) not null
        constraint fk2o0jvgh89lemvvo17cbqvdxaa
            references users,
    role_id varchar(255) not null
        constraint fkj6m8fwv7oqv74fcehir1a9ffy
            references roles,
    primary key (user_id, role_id)
);

create table verification_token
(
    id          bigint       not null
        primary key,
    expiry_date timestamp,
    token       varchar(255),
    user_id     varchar(255) not null
        constraint fk3asw9wnv76uxu3kr1ekq4i1ld
            references users
);