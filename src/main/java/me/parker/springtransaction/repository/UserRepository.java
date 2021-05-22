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
            .age(rs.getInt("age"))
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

    public void updateAgeById(int age, Long userId) {
        jdbcTemplate.update("UPDATE users SET age=? WHERE id=?", age, userId);
    }

    public void updateById(String name, int age, Long userId) {
        jdbcTemplate.update("UPDATE users SET name=?, age=? WHERE id=?", name, age, userId);
    }
}
