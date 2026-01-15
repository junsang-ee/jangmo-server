# Jangmo(장모) - 풋살 매치 관리 플랫폼 (Backend)

**Jangmo**는 매주 풋살 및 축구 경기를 진행하는 팀의 **경기 인원 모집, 매치 확정, 구장 관리, 투표 시스템** 등 을 웹 기반으로 제공하는 플랫폼입니다.  
카카오톡 오픈채팅에서 진행되던 수작업 운영을 시스템화하여 효율성을 높이며 추후에 더 편리하고 활발한 소통 및 모임 활동을 기대하고 있습니다.

> **백엔드 전담 개발 / 기획 리드와 화면(Front) 구성 및 개발**  
> 실제 운영 중인 풋살 팀의 문제를 분석하고, **직접 기획 및 개발**  
> 팀원들과 피드백을 주고받으며 기능 우선순위와 흐름을 조율하며 개발중

---

## 주요 기능

- **역할 기반 권한 분리 (역할 별 기능 분리)**
- **인증 관련 SMS 및 Cache 활용** 
- **경기 참여 투표 기능** (오전 / 오후 / 불참 등)
- **매치 생성 및 확정 프로세스**
- **자주 이용하는 구장 등록 및 관리(Kakao API 를 활용한 Map 시각화)**
- **용병 관리(1회 참여) 기능**
- **JWT 기반 인증 및 보안**
- **공지 사항 및 자유게시판 기능 (예정)**
- **RESTful API 설계 및 Swagger 문서화 (예정)**
- **AWS 기반 배포 및 Jenkins 자동화 (예정)**
- **추가 "쉬어가기" 주 기능 외 작업(희망 번호 제외 로또 번호 추출 서비스) (예정)**
---

## 기술 스택

| 영역       | 기술                                                                        |
|----------|---------------------------------------------------------------------------|
| Language | Java 21 (11 => 21, upgradedAt. 25.10.23)                                  |
| Backend  | Spring Boot 3.5.6 (2.7.4 => 3.5.6, upgradedAt. 25.10.23), Spring Security |
| DB       | PostgreSQL, JPA (Hibernate 6, (5 => 6, upgradedAt. 25.10.23))             |
| Auth     | JWT (JSON Web Token)                                                      |
| DevOps   | AWS(EC2, RDS) Jenkins(예정)                                                 |
| Docs     | Swagger (Springdoc) (예정)                                                  |

## 프로젝트 배경

서울·경기권에서 활동 중인 풋살팀은 약 20명 정도로 구성되어 있으며,  
기존에는 **카카오톡 오픈채팅으로만 매치 일정과 인원 조율**이 이루어졌습니다.

- 매주 투표 참여 누락 → 경기 진행 실패 사례
- 비밀 번호가 존재하는 구장 시(구장에서 매주 변경) 일일이 확인 하여 찾아 들어 가는 이슈 존재
- 경기 시간·장소 확인 불편 → 늦은 도착, 누락
- 용병 섭외·기록 누락 → 기록 관리 어려움
- 공지·기록 관리 불가 → 정보의 불투명함

5가지 등의 여러 비효율적인 운영을 해결하기 위해 **기획부터 개발까지의 모든 과정**을 주도하고 개발중에 있습니다.

---

## 진행 상황
- [x] 로그인 및 회원가입(회원, 용병) 기능 완료 (security, JWT)
- [x] 비밀번호 찾기 및 변경 기능 완료 (Sms)
- [x] API 공통 응답 구조화 완료
- [x] CustomException HttpStatus 및 ErrorMessage 구축 완료
- [x] Kakao Map API 및 Sms API 연결 완료
- [ ] 투표 / 매치 / 경기장 도메인 설계 디테일 하게 구축 예정
- [ ] 투표와 매치 생성 시 추후 개발 될 Board, Post 연결 구상 중
- [ ] Swagger 문서 작성 중
- [ ] CI/CD 자동 배포 구축 예정

---

## 백엔드 개발 주요 포인트

- **DB 설계**: 역할 기반 유저 구조, 투표-매치-경기장 관계 명확화
- **보안**: Spring Security + JWT 기반 로그인 / 인증처리
- **유지보수**: 엔티티 구조를 단순화하여 추후 확장 대비
- **단일 책임 원칙**을 고려한 레이어 구조
- 단위 테스트 기반의 **TDD 개발 프로세스 적용**으로(~TDD ~ing) 서비스 로직의 안정성과 유지보수성 확보 
---

## 프로젝트 구조

```bash
src
├── config            # 시큐리티, JWT 설정, Sms, Message 정의
├── constants         # Error/Success Message 및 Enum 정의
├── controller        # REST API 컨트롤러
├── exception         # Custom Exception 및 Handler 정의
├── infra             # Cache, SMS, WebClient Provider
├── model             # Entity, DTO(요쳥/응답), VO
├── repository        # JPA Repository
├── security          # Custom UserDetail, UserDetailService (extends) 
├── service           # 비즈니스 로직
├── util              # 유틸리티 클래스