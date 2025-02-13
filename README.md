# CANBEJ - 일정 공유 & 관리 웹서비스

## :pushpin: 프로젝트 개요
**CANBEJ**는 다양한 일정 및 장소를 효과적으로 공유하고 관리할 수 있도록 돕는 웹 서비스입니다. 사용자는 개인 또는 그룹 일정 관리를 수행할 수 있으며, **지도 기반 위치 공유 기능**을 통해 편리하게 일정을 조율할 수 있습니다.

## :busts_in_silhouette: 팀 소개
### **TEAM 10**
| 이름     | 역할               | 담당 업무                        |
|---------|------------------|-------------------------------|
| 유영주 | 팀장              | 캘린더 API, 전체 연결 담당      |
| 박찬호   | 팀원              | 캘린더 API, 전체 연결 담당      |
| 황인우   | 팀원              | 로그인 (Spring Security) 담당  |
| 민태희   | 팀원              | 로그인 (Spring Security) 담당  |
| 김혜윤   | 팀원              | 스케줄, 지도 API 담당          |
| 권기용   | 팀원              | 스케줄, 지도 API 담당          |
| **JH**   | 멘토              | 프로젝트 피드백 및 질의응답 지원 |

## :dart: 주요 기능
### **:white_check_mark: 사용자 계정 관리**
- **소셜 로그인 (구글, 카카오) 지원**
- **JWT 기반 인증 및 인가** (Refresh Token 사용)
- 닉네임 변경 및 금지어 설정
- 장기 미사용 계정 **휴면 처리**

### **:calendar: 캘린더 기능**
- **캘린더 별 권한 관리** (캘린더 소유자만 수정/삭제 가능)
- **FullCalendar API** 연동 (월별/주별 보기 지원)
- **개인 및 그룹 캘린더** 생성 및 관리

### **:pushpin: 일정 관리 기능**
- 특정 캘린더 일정만 조회 가능
- **일정 시간 검증** (시작 시간과 종료 시간 유효성 검사)
- 일정 상세 정보 조회 (제목, 설명, 위치, 시간 등)
- 일정 **CRUD** 기능 지원 (생성, 조회, 수정, 삭제)
- **일정 권한 관리** (캘린더 소유자 및 참가자 역할 구분)

### **:world_map: 지도 연동 (Naver Map API)**
- **위치 기반 일정 관리 지원**
- 마커 추가 및 위치 검색 (**Geocoding, Reverse Geocoding**)
- **지도에서 일정 위치 시각화**

### **:lock: 관리자 기능**
- **사용자 계정 관리 (검색, 페이징 지원)**
- **계정 잠금 및 복구 기능** (비밀번호 5회 실패 시 계정 잠금, 이메일 인증 필요)
- **보안 강화** (비밀번호 해싱, JWT 인증 시스템 적용)

## :card_file_box: ERD (Entity Relationship Diagram)
![최종ERD](https://github.com/user-attachments/assets/36de3922-e62b-4e69-afb8-6b80571bdcad)


## :hammer_and_wrench: 기술 스택
- ### **Frontend**
<img src="https://skillicons.dev/icons?i=nextjs,react,typescript,tailwind" />


- ### **Backend**
<img src="https://skillicons.dev/icons?i=java,spring,hibernate" />


- ### **Database**
<img src="https://skillicons.dev/icons?i=redis, h2" />


### **DevOps**
<img src="https://skillicons.dev/icons?i=gradle,github" />


## :pushpin: API 및 주요 연동
- **FullCalendar API**: 캘린더 일정 표시 (월별/주별 보기 지원)
- **Naver Map API**: 일정 위치 마커 및 검색 지원
- **JWT 인증 & Refresh Token**
- **Redis 기반 계정 잠금 & 세션 관리**

## :link: Git 협업 전략
- **Git Flow 기반 브랜치 관리**
- **개발 완료 후 `develop` 브랜치에서 테스트 진행 후 `main`에 병합**
- **GitHub 프로젝트 및 Issues 활용하여 태스크 관리**

## :rocket: 향후 보완할 점
- **친구 추가 및 일정 공유 기능**
- **실시간 채팅 기능 추가** (WebSocket 활용)
- **캘린더와의 실시간 연동 기능 강화**

---

CANBEJ 프로젝트는 **일정 관리와 장소 공유를 한 번에 해결**할 수 있도록 설계되었습니다. 사용자의 **편의성과 보안**을 최우선으로 고려하며, 지속적인 개선을 통해 완성도를 높여 나갈 예정입니다. :dart:

---

### 프로젝트 관리 및 문서화
- [프로젝트 관리 Notion](https://www.notion.so/10-5d52f2cbeb374740874572de5fea184b?pvs=4)
- [CANBEJ_발표 ppt](https://github.com/user-attachments/files/18776426/CANBEJ_._.MAX.pdf)
- [CANBEJ_시연 영상](https://youtu.be/d9QJ5sefXTc)


## 📄 Git Commit Convention
| 태그 이름 | 설명 |
| --- | --- |
| Feat | 새로운 기능을 추가할 경우 |
| Fix | 버그를 고친 경우 |
| Design | CSS 등 사용자 UI 디자인 변경 |
| !BREAKING CHANGE | 커다란 API 변경의 경우 |
| !HOTFIX | 급하게 치명적인 버그를 고쳐야하는 경우 |
| Style | 코드 포맷 변경, 세미 콜론 누락, 코드 수정이 없는 경우 |
| Refactor | 프로덕션 코드 리팩토링 |
| Comment | 필요한 주석 추가 및 변경 |
| Docs | 문서를 수정한 경우 |
| Test | 테스트 추가, 테스트 리팩토링(프로덕션 코드 변경 X) |
| Chore | 빌드 태스트 업데이트, 패키지 매니저를 설정하는 경우(프로덕션 코드 변경 X) |
| Rename | 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우 |
| Remove | 파일을 삭제하는 작업만 수행한 경우 |



