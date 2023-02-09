create table User {
    id bigint primary key auto_increment,
    username        varchar(100),
    tel         varchar(100) unique,
    created_at  timestamp,
    updated_at  timestamp
}