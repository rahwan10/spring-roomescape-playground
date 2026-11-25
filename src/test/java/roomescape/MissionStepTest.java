package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 오단계_H2에_예약_테이블을_생성한다() throws SQLException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(
                    connection.getMetaData().getTables(null, null, "RESERVATION", null).next())
                    .isTrue();
        }
    }

    @Test
    void 육단계_데이터베이스에_저장된_예약을_API에서_조회한다() {
        jdbcTemplate.update("INSERT INTO reservation(name, date, time) VALUES (?, ?, ?)",
                "브라운", "2023-08-05", "15:40");

        int databaseCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM reservation", Integer.class);
        RestAssured.get("/reservations").then().statusCode(200)
                .body("size()", is(databaseCount), "[0].name", is("브라운"));
    }

    @Test
    void 칠단계_API로_추가하고_삭제한_예약이_데이터베이스에_반영된다() {
        Map<String, String> request = Map.of(
                "name", "브라운", "date", "2023-08-05", "time", "10:00");
        RestAssured.given().contentType(ContentType.JSON).body(request)
                .post("/reservations").then().statusCode(201)
                .header("Location", "/reservations/1").body("id", is(1));
        assertThat(reservationCount()).isEqualTo(1);

        RestAssured.delete("/reservations/1").then().statusCode(204);
        assertThat(reservationCount()).isZero();
    }

    private int reservationCount() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM reservation", Integer.class);
    }

    @Test
    void 일단계() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.HTML)
                .body(containsString("어드민 페이지"));
    }

    @Test
    void 이단계_예약_화면과_빈_목록을_조회한다() {
        RestAssured.get("/reservation").then().statusCode(200)
                .contentType(ContentType.HTML).body(containsString("예약 관리"));
        RestAssured.get("/reservations").then().statusCode(200)
                .contentType(ContentType.JSON).body("size()", is(0));
    }

    @Test
    void 삼단계_예약을_추가하고_취소한다() {
        Map<String, String> request = Map.of("name", "브라운", "date", "2023-08-05", "time", "15:40");

        RestAssured.given().contentType(ContentType.JSON).body(request)
                .post("/reservations")
                .then().statusCode(201).header("Location", "/reservations/1")
                .body("id", is(1), "name", is("브라운"),
                        "date", is("2023-08-05"), "time", is("15:40"));

        RestAssured.get("/reservations").then().statusCode(200).body("size()", is(1));
        RestAssured.delete("/reservations/1").then().statusCode(204);
        RestAssured.get("/reservations").then().statusCode(200).body("size()", is(0));
    }

    @Test
    void 사단계_잘못된_추가_요청은_저장하지_않는다() {
        Map<String, String> request = Map.of("name", "브라운", "date", "", "time", "");

        RestAssured.given().contentType(ContentType.JSON).body(request)
                .post("/reservations")
                .then().statusCode(400).body("message", is("예약자, 날짜, 시간을 입력해 주세요."));

        RestAssured.get("/reservations").then().statusCode(200).body("size()", is(0));
        RestAssured.delete("/reservations/1").then().statusCode(404);

        Map<String, String> validRequest = Map.of(
                "name", "브라운", "date", "2023-08-05", "time", "15:40");
        RestAssured.given().contentType(ContentType.JSON).body(validRequest)
                .post("/reservations")
                .then().statusCode(201).body("id", is(1));
    }

    @Test
    void 사단계_누락하거나_공백만_보낸_필수값도_거절한다() {
        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("name", "브라운", "date", "2023-08-05"))
                .post("/reservations").then().statusCode(400);

        RestAssured.given().contentType(ContentType.JSON)
                .body(Map.of("name", "  ", "date", "2023-08-05", "time", "15:40"))
                .post("/reservations").then().statusCode(400);

        RestAssured.get("/reservations").then().statusCode(200).body("size()", is(0));
    }
}
