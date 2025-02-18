


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

-- 네이버로그인 프로필
CREATE TABLE userProfile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    oauthId VARCHAR(255),
    userId VARCHAR(255),
    name VARCHAR(255),
    email VARCHAR(255)
);

-- 채팅
CREATE TABLE messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 메시지 읽음 여부 확인
ALTER TABLE messages ADD COLUMN isRead BOOLEAN DEFAULT FALSE;

table member {
  id bigint [primary key]
  userId varchar [not null, unique]
  userPw varchar [not null]
  email varchar [not null, unique]
  name varchar [not null]
}

table board {
  bno bigint [primary key]
  writer varchar
  title varchar
  content varchar
  modifiedDate timestamp
  regDate timestamp
  password varchar
  adminComment varchar
}

table product {
  id bigint [primary key]
  name varchar
  image varchar
  seller_name varchar
  location varchar
  deadlineDate date
  applicants integer
  max_applicants integer
  description text
  activity_type varchar
  owner_id bigint [ref: > member.id]
  category_id bigint [ref: > category.id]
}

table userProfile {
  id bigint [primary key]
  email varchar
  name varchar
}

table applications {
  id bigint [primary key]
  member_id bigint [ref: > member.id]
  product_id bigint [ref: > product.id]
  application_date timestamp
  status varchar
  phone varchar
}

table category {
  id bigint [primary key]
  name varchar
}

table member_favorite {
  member_id bigint [ref: > member.id]
  product_id bigint [ref: > product.id]
}

table messages {
  id bigint [primary key]
  sender_id bigint [not null]
  receiver_id bigint [not null]
  content text [not null]
  isRead boolean
  timestamp timestamp
}