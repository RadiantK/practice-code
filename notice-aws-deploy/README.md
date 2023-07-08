# Notice-Project

AWS 배포용 게시판 예제 프로젝트
<br/> 
<br/> 

### AWS RDS 사용 시 application.properties 파일 수정

```properties
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.url=jdbc:mariadb://[RDS엔드포인트]:[포트번호]/[DB명]?characterEncoding=utf-8
spring.datasource.username=[사용자명]
spring.datasource.password=[비밀번호]
```
<br/>

### 예제에 사용된 테이블 참조
- src/main/resources/mariadb.sql 파일 확인한다.
- 회원가입 기능은 구현하지 않았기 때문에 파일 하단에 있는 insert문으로 예제용 회원 생성 후 코드 이용하면 된다.
- sql 파일에 있는 프로시저를 꼭 등록 후 사용해야 한다.
