# 💎 TrialTrove Project
## 블로그 체험단 사이트

### 💍기술
* SpringBoot3
* Spring Data JPA
* Spring Security6
* OAuth2.0
* JAVA 17
* Thymeleaf
* JavaScript
* H2
* Gradle
* Lombok
* JUnit(test)
* JavaMailSender(이메일 전송)
* Web Socket
* STOMP
* QueryDSL
* Docker(Compose)

<br/> <br/>



## 📌 Project Summary
- Spring Boot 3 기반 웹 애플리케이션
- Spring Security + OAuth2 기반 사용자 인증
- WebSocket 기반 실시간 채팅 기능 구현
- Docker를 활용한 서비스 배포 경험 (재현 가능한 배포 환경 구성)
- 개인 프로젝트

<br/> <br/>


## 🔍 Technical Decisions

### 1️⃣ 실시간 채팅 – WebSocket + STOMP
- 문제 상황:
  다수 사용자가 동시에 참여하는 채팅 기능에서
  HTTP Polling 방식은 메시지가 없을 때도 요청이 반복되어 서버 자원을 비효율적으로 사용한다고 판단
- 대안:
  Polling / Long Polling / WebSocket
- 선택:
  WebSocket + STOMP
- 선택 이유:
  양방향 통신을 통해 실시간 메시지 전달이 가능하며,
  이벤트 기반 구조로 불필요한 요청을 줄일 수 있음.
  향후 사용자 증가 시 Redis Pub/Sub 등 메시지 브로커와 연계 가능한 확장성도 고려함.

---

### 2️⃣ 배포 – Docker & Docker Compose
- 문제 상황:
  개인 프로젝트를 실제 서버 환경에 배포해보는 것이 목표였으며,
  배포 과정을 반복적으로 재현하고 관리할 방법이 필요했음
- 선택:
  Docker 기반 컨테이너 배포
- 선택 이유:
  배포 과정을 코드로 관리해 재현 가능한 배포 환경을 구성하고,
  Docker Compose로 서비스 실행을 일관되게 관리하기 위함

---

### 3️⃣ 인증/인가 – Spring Security 6 + OAuth2
- 문제 상황:
  자체 인증 로직 구현 시 보안 취약점 및 유지보수 부담 발생 가능
- 선택:
  Spring Security 6 + OAuth2
- 선택 이유:
  검증된 보안 프레임워크를 활용해 인증/인가 로직을 안정적으로 처리하고,
  OAuth2 기반 소셜 로그인을 통해 사용자 편의성을 향상시킴.

---

### 4️⃣ 검색 기능 – QueryDSL
- 문제 상황:
  검색 조건이 늘어날수록 JPQL 문자열 기반 쿼리의 가독성과 유지보수성이 저하됨
- 선택:
  QueryDSL
- 선택 이유:
  타입 안전한 쿼리 작성으로 컴파일 단계에서 오류를 방지하고,
  BooleanBuilder를 활용한 동적 쿼리 구성으로 검색 로직의 확장성을 확보함.




<br/> <br/> <br/> <br/>

## ⭐️시연 영상⭐️

## 홈화면
    - 홈화면 : 카테고리별 상품 검색

<details><summary>📸 홈 화면 </summary>

    
![홈화면](https://github.com/user-attachments/assets/c5fced8f-3678-441a-bfbd-a185832694d2) </details>


<br/> <br/>

 ##  회원, 관리자(User, Admin)
    - 회원 : 회원가입 및 탈퇴 / 로그인 및 로그아웃 / 네이버 로그인 / 내 정보 / 아이디, 비밀번호 찾기
<details><summary>📸 *회원가입* / *로그인* / *네이버로그인* </summary>
    
![회원가입:로그인](https://github.com/user-attachments/assets/27db1f0c-5900-4b54-8bb7-adb7b6df2cbf)

</details>

<br/> <br/>

![네이버로그인](https://github.com/user-attachments/assets/7bfc3626-4ba0-423b-a1be-106cdb86989b)

<br/> <br/>


<details><summary>📸 *내정보 수정* </summary>
    
![내정보업데이트](https://github.com/user-attachments/assets/bed20959-3062-4294-8efe-79e3d0566ad7)
</details>

<br/>
<details><summary>📸 *아이디, 비밀번호 찾기* </summary>
    
![아이디찾기](https://github.com/user-attachments/assets/d2905468-de04-4d80-b1b0-7f16b2b868a9)
![비밀번호찾기](https://github.com/user-attachments/assets/e76b9837-a4df-4f22-b5cf-398e8df5947e)
</details>


<br/> <br/> <br/> 
<br/> <br/>

    - 관리자 : 관리자로 로그인 했을 경우에만 보이는 메뉴 구현(상품 등록 / 삭제 / 신청현황 관리 / 게시판 답변)
<details><summary>📸 *상품등록/수정/삭제* </summary> 
<br/> <br/>
    
![관리자상품등록수정삭제](https://github.com/user-attachments/assets/329d4c32-013f-4827-bf72-331c875d0978)
</details>


<br/>

<details><summary>📸 *신청현황 관리* </summary> <br/> <br/>
    
![관리자신청현황](https://github.com/user-attachments/assets/28365b72-2d02-4987-9b57-7120de87698f)</details>


<br/> <br/>

👉🏻 *실시간 채팅*
<br/> <br/>
![실시간채팅(1)](https://github.com/user-attachments/assets/55743f21-cc12-44e4-8980-246d1f749268)
![실시간채팅(2)](https://github.com/user-attachments/assets/49f5638a-4016-4e27-b040-2424c86e9e6b)





<br/> <br/>

 ## 상품(product)
    - 상품 신청 / 장바구니 / 상품 카테고리 검색
 <details><summary>📸 *신청* </summary>  
<br/> <br/>
     
![상품신청](https://github.com/user-attachments/assets/f117bd08-f5b9-4610-a9c2-f5268472b401)</details>


<br/> <br/> 
 ##  장바구니(Cart)
    - 장바구니 담기 / 조회 / 삭제
<details><summary>📸 *장바구니* </summary> 

![보물함](https://github.com/user-attachments/assets/496116b7-e935-4072-a64e-45baaf4b993b)</details>

     
<br/> <br/> <br/>     
 ## 검색(Search)
    - 키워드 검색 / 카테고리 검색
![상품 카테고리,검색](https://github.com/user-attachments/assets/6f60be36-3949-4732-a271-9b868c861a5e)

<br/> <br/>     
 ##  게시판(Notice, FAQ)
    - 관리자만 답변 기능 부여
 <details><summary> *게시글 등록 / 수정 / 삭제* </summary> 
     
![게시판등록수정삭제](https://github.com/user-attachments/assets/38c8e54c-eeed-4a9b-922d-203563770fc2)
 </details>







