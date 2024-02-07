

--관리자
CREATE TABLE admin (
    adminId INT PRIMARY KEY AUTO_INCREMENT,
    adminPw VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

--회원
CREATE TABLE member (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userId VARCHAR(255) NOT NULL,
    userPw VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL
);

--게시판
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

ALTER TABLE board
ADD COLUMN adminComment BOOLEAN DEFAULT FALSE;



