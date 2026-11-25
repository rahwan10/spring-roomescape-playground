package roomescape;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/** 예약 테이블을 읽고 쓰는 SQL을 한곳에 모은다. */
@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /** SELECT 결과의 각 행을 예약 객체로 바꿔 목록으로 반환한다. */
    public List<Reservation> findAll() {
        String sql = "SELECT id, name, date, time FROM reservation ORDER BY id";
        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> new Reservation(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("date"),
                resultSet.getString("time")));
    }

    /** INSERT 후 데이터베이스가 만든 ID를 받아 생성 응답에 쓸 예약을 반환한다. */
    public Reservation save(ReservationRequest request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO reservation(name, date, time) VALUES (?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, request.name());
            statement.setString(2, request.date());
            statement.setString(3, request.time());
            return statement;
        }, keyHolder);
        Number id = Objects.requireNonNull(keyHolder.getKey(), "생성된 예약 ID가 없습니다.");
        return new Reservation(id.longValue(), request.name(), request.date(), request.time());
    }

    /** DELETE가 실제로 삭제한 행이 있는지 반환한다. */
    public boolean deleteById(long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }
}
