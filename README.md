# community
Community REST API Server (Main project)


### 프로젝트 설명

- 커뮤니티 API 서버 제작 개인 프로젝트입니다.
- 성능 튜닝을 위해 JPA 설계와 테이블 설계에 집중했습니다.
- JPQL을 사용하여 N+1문제를 해결하였습니다.
- 여러가지 기술 적용으로 역량 상승을 위해 제작한 프로젝트입니다.
- 클린코드 원칙에 입각하여, 프로젝트를 타 개발자도 쉽게 읽을 수 있도록 노력했습니다.
- 테스트 코드의 다양한 케이스를 작성하며 서비스의 안정도를 높이기 위해 노력했습니다.

### Skills

- Spring Web
- JPA
- JPQL
- JUnit5 Test
- Spring Security
- JWT
- MySQL Driver
- Validation
- Swagger

### API 설명
- Auth : 회원가입 및 Spring Security + JWT 로그인 
- Member : 전체 및 개별 유저 조회, 즐겨찾기 한 글 목록 조회, 유저 정보 관련 CRUD
- Category : 계층형 카테고리 구현
- Board : 게시글 CRUD 및 검색 기능 추가, 이미지 업로드, 게시글 좋아요 및 즐겨찾기 기능
- Comment : 댓글 조회, 작성, 삭제
- Message : 쪽지 CRUD, 받은 쪽지와 보낸 쪽지 사이 둘다 삭제되면 테이블에서 Column 제거
- Report : 유저 및 게시글, 댓글 신고 관리 기능
- Admin : 관리를 위한 기능

## 테스트 결과

<img width="638" alt="스크린샷 2023-03-11 오후 10 54 01" src="https://user-images.githubusercontent.com/107785279/224488728-da2a4ec7-3b16-44e6-837a-f516f1542266.png">


## 테스트 커버리지

<img width="577" alt="스크린샷 2023-03-11 오후 10 59 20" src="https://user-images.githubusercontent.com/107785279/224488925-0d23d014-a99e-4b42-994d-fdac0cd181cb.png">





### Git Convention

- feat : 기능추가
- fix : 버그 수정
- refactor : 리팩토링, 기능은 그대로 두고 코드를 수정
- style : formatting, 세미콜론 추가 / 코드 변경은 없음
- chore : 라이브러리 설치, 빌드 작업 업데이트
- docs : 주석 추가 삭제, 문서 변경


