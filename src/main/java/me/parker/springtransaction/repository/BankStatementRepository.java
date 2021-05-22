package me.parker.springtransaction.repository;

import lombok.RequiredArgsConstructor;
import me.parker.springtransaction.domain.BankStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BankStatementRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<BankStatement> bankStatementMapper = ((rs, rowNum) -> BankStatement.builder()
            .id(rs.getLong("id"))
            .fromUserId(rs.getLong("from_user_id"))
            .toUserId(rs.getLong("to_user_id"))
            .amount(rs.getLong("amount"))
            .build());

    public BankStatement findById(Long bankStatementId) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM bank_statements WHERE id = ?",
                bankStatementMapper,
                bankStatementId);
    }

    public void insert(Long fromUserId, Long toUserId, long amount) {
        jdbcTemplate.update("INSERT INTO bank_statements (id, from_user_id, to_user_id, amount)" +
                "VALUES(null, ?, ?, ?)",
                fromUserId, toUserId, amount);
    }

    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM bank_statements");
    }
}
