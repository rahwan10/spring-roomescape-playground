package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
}
