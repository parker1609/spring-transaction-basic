# 스프링 트랜잭션 살펴보기

## Transaction Propagation (트랜잭션 전파)

### 참고사항

#### H2 데이터베이스 isolation default level

> Isolation
For H2, as with most other database systems, the default isolation level is 'read committed'. This provides better performance, but also means that transactions are not completely isolated. H2 supports the transaction isolation levels 'serializable', 'read committed', and 'read uncommitted'.
> http://repository.transtep.com/repository/thirdparty/H2/1.0.63/docs/html/advanced.html

#### Spring JdbcTemplate `queryForObject` deprecated method

```java
@Deprecated
 @Nullable
public <T> T queryForObject(String sql,
                           @Nullable Object[] args,
                           RowMapper<T> rowMapper)
                    throws DataAccessException
```

<https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html#queryForObject-java.lang.String-java.lang.Object:A-org.springframework.jdbc.core.RowMapper->

```java
@Nullable
<T> T queryForObject(String sql,
                    RowMapper<T> rowMapper,
                    @Nullable Object... args)
        throws DataAccessException
```

<https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/core/JdbcOperations.html#queryForObject-java.lang.String-org.springframework.jdbc.core.RowMapper-java.lang.Object...->

#### 트랜잭션 전파 레벨 관련
- <https://deveric.tistory.com/86>
- <https://woowabros.github.io/experience/2019/01/29/exception-in-transaction.html>