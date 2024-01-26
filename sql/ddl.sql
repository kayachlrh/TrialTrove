
CREATE TABLE member (
    email VARCHAR(100) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    pwd VARCHAR(100) NOT NULL,
    repwd VARCHAR(100) NOT NULL
);

create table board(
    bno int auto_increment,
    title varchar(150),
    content varchar(2000),
    writer varchar(50),
    regdate timestamp default now(),
    password varchar(100),
    modifiedDate timestamp,
    constraint pk_board PRIMARY key(bno)
);

ALTER TABLE board
ADD COLUMN is_deleted BOOLEAN DEFAULT false;
