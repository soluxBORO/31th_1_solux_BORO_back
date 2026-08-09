# 31th_1_solux_BORO_back
## 🌀 BORO (바로)
> 바로 송이끼리! 대학생만의 신뢰 기반 대여 플랫폼

대학생들이 물품이나 빈자리를 등록하고,    
요청에 대한 승인·거절 및 이어지는 채팅을 통해 안전하게 대여하고 공유할 수 있는 신뢰 기반 대여 플랫폼

<img width="1056" height="761" alt="썸네일 이미지" src="https://github.com/user-attachments/assets/53f20e18-2dce-4239-b187-3172402394dc" />

## 🗂 URL
- https://api.boro-app.com/swagger-ui/index.html
- https://31th-1-solux-boro-front-three.vercel.app/


## 💻 팀원 소개 

<div align="left">

| 제유진 (백엔드장) | 이채연 (팀원) |
| :---: | :---: |
| [@yoojinche](https://github.com/yoojinche) | [@chaeyeon613](https://github.com/chaeyeon613) |
| 대여, 채팅, 회원, 인증, 아이템 구매 | 물품, 빈자리 게시글, 배포 |

</div>

## 🗄️ ERD
<img width="1361" height="600" alt="image" src="https://github.com/user-attachments/assets/35af28c4-1620-46e6-a890-b2139867146b" />

## 🛠 기술 스택

| 구분 | 스택 |
| :---: | :---: |
|**Language & Framework** | Java 17 / Spring Boot 3.5.14 / Spring MVC / Spring Data JPA |
| **DB / Storage** | MySQL/ Redis / AWS RDS, S3 |
| **Communication / Messaging** | WebSocket / STOMP / Redis Pub/Sub |
| **Infra / CI-CD** | GitHub Actions / Docker / AWS EC2, Route53 |
| **Security** | 	Spring Security / OAuth 2.0 (Google) / JWT |
| **Documentation** | 	Swagger |



## 📂 프로젝트 구조
```text
31th_1_solux_BORO_back/
├── monitoring/
│   └── prometheus.yml
└── src/main/java/com/boro/
    │   ├── BoroApplication.java
    │   ├── domain/
    │   │      ├── auth/ # 소셜 OAuth2 로그인, 인증
    │   │      ├── chat/  # 채팅
    │   │      ├── emptyspot/ # 빈자리
    │   │      ├── member/  # 회원 및 아이템 구매
    │   │      ├── post/  # 물품
    │   │      │── rentalrequest/  #대여
    │   ├── global/
    │   │      ├── common/  # 공통 엔티티
    │   │      ├── config/  # 설정
    │   │      ├── data/  # 상수
    │   │      ├── error/  # 에러 핸들링
    │   │      ├── s3/  # s3
    │   │      ├── security/  # 인증
    │   └── resources/
    │       ├── application-develop.yml
    │       ├── application-local.yml
    │       └── application.yml
```



## 📋 Commit Message Convention (이모지 + 소문자 적용)
| Tag         | Description             |
|------------|-------------------------|
| `✨ feat`    | 새로운 기능 추가        |
| `🐛 fix`     | 버그 수정              |
| `📝 docs`    | 문서 추가, 수정, 삭제  |
| `🧪 test`    | 테스트 코드 추가, 수정, 삭제 |
| `🎨 style`   | 코드 형식 변경         |
| `♻️ refactor` | 코드 리팩토링         |
| `⚡ perf`    | 성능 개선              |
| `🏗️ build`  | 빌드 관련 변경사항     |
| `⚙️ ci`      | CI 관련 설정 수정     |
| `🚀 chore`   | 기타 변경사항          |
