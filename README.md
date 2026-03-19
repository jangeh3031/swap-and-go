# swap-and-go

중고 거래(RESALE)와 대여(RENTAL)를 함께 다루는 거래 플랫폼 백엔드 API입니다.

## Tech Stack
- Java 17, Spring Boot 4
- Spring Security, JWT
- Spring Data JPA, Querydsl
- Redis (이메일 인증)
- AWS S3 (이미지 업로드)
- H2 / MySQL
- Web Socket (채팅 구현중)

## 주요 기능
- 이메일 인증 기반 회원가입/로그인
- JWT 인증/인가, 리프레시 토큰
- 중고/대여 상품 등록·수정·삭제
- 검색(커서 기반 페이지네이션)
- 거래 요청/수락/거절
- 거래(대여) 조기 반납 플로우
- 신고/관리자 처리
- 찜 기능
- 사용자간 채팅 기능
