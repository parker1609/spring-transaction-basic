package me.parker.springtransaction.repository;

import lombok.RequiredArgsConstructor;
import me.parker.springtransaction.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private RowMapper<User> userMapper = (rs, rowNum) -> User.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .balance(rs.getLong("balance"))
            .build();

    public User findById(Long userId) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = ?",
                userMapper,
                userId);
    }

    public void updateNameById(String name, Long userId) {
        jdbcTemplate.update("UPDATE users SET name=? WHERE id=?", name, userId);
    }

    public void updateBalanceById(long balance, Long userId) {
        jdbcTemplate.update("UPDATE users SET balance=? WHERE id=?", balance, userId);
    }

    public void updateById(String name, long balance, Long userId) {
        jdbcTemplate.update("UPDATE users SET name=?, balance=? WHERE id=?", name, balance, userId);
    }
}
