package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

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
}
