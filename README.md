# 송금(뿌리기) REST API 구현
### kakaotalk에 있는 송금-뿌리기 pay 기능 구현
#### 사용 기술 스택
- Spring boot(Java 8)
- MySQL
- JPA
- Spring Web(MVC)

#### 요구사항 분석
- 서버 구성은 HA를 위해 다수의 인스턴스가 동작한다고 가정
- 해당 기능은 지속적으로 트래픽이 증가할 예정
- 송금-뿌리기 생성, 받기, 조회 기능 구현

#### 구현 전략
- Rest API로 작성
- 다수의 인스턴스 서버에서 작동하기 위해서는 상태값 저장 및 조회를 위한 persistence가 필요(MySQL DB 사용)
- 데이터 저장을 위한 2개의 테이블 존재(뿌리기 상태값 저장 테이블인 distribute와 뿌리기 돈 인원만큼 분배 저장 distribute_state 테이블)  
- 두 테이블은 1:N 연관관계 매핑 (distribute(1) - distribute_state(N))
- 스키마는 schema.sql를 통해 서버 실행 시 자동 생성(스키마가 존재하지 않을 경우)

##### 뿌리기 생성(Post- /api/distribution)
- POST method를 통한 뿌리기 데이터 생성
- UUID를 3자리로 자른 랜덤 토큰 생성
- 요청값에 대해 distribute 테이블 저장(user id, room id, 뿌릴 돈, 뿌릴 인원, 토큰, 뿌린 시각)
- 뿌릴 돈을 인원만큼 랜덤하게 할당
- distribute_state 테이블에 뿌릴 인원만큼 레코드 저장(토큰, 할당된 돈, 할당 상태, 할당된 user id)
- 데이터 처리 후 token 반환

##### 뿌리기 받기(Put- /api/distribution/{token})
- PUT method를 통한 분배된 돈 상태 업데이트
- 요청 토큰 값으로 뿌리기 데이터 조회
- 조회한 뿌리기 데이터에 대한 제약사항 검증(뿌리기당 한 사용자는 한번, 동일한 대화방, 뿌리기 10분간만 유효, 자신이 뿌린 건은 못받음)
- 아직 할당되지 않은 분배 돈이 있는지 확인
- 할당되지 않은 금액 중 하나를 반환하고 해당 레코드는 할당 완료 상태와 함께 수령한 user id 입력

##### 뿌리기 조회(Get- /api/distribution/{token})
- GET method를 통한 뿌리기 조회
- 요청 토큰 값으로 뿌리기 데이터 조회
- 요청 user id와 조회 결과 뿌린 user id가 동일한지 확인
- 뿌리기 저장 시간과 현재 시간을 비교하여 7일 이내인지 확인
- distribute와 distribute_state의 연관관계 매핑을 통한 데이터 조회 반환(뿌린시각, 뿌린금액, 받기완료된 금액, 받기 완료된 정보 리스트)

