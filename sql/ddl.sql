


--회원
CREATE TABLE member (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userId VARCHAR(255) NOT NULL,
    userPw VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL
);

--네이버 로그인 정보
CREATE TABLE member_naver (
    naverId VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

--네이버 매핑
CREATE TABLE member_naver_mapping (
    naver_id VARCHAR(255) NOT NULL,
    member_id INT NOT NULL,
    PRIMARY KEY (naver_id, member_id),
    FOREIGN KEY (naver_id) REFERENCES member_naver(naver_id),
    FOREIGN KEY (member_id) REFERENCES member(id)
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

-- 관리자 추가
ALTER TABLE member ADD COLUMN is_admin BOOLEAN DEFAULT FALSE;

ALTER TABLE board ALTER COLUMN adminComment VARCHAR(255);


