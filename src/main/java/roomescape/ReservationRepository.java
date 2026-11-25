package roomescape;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
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
}
