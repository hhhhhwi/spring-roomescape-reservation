package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservationTime.ReservationTimeRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("예약 시간 관련 api 호출 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeAcceptanceTest {
	private final ReservationTimeRequest request = new ReservationTimeRequest("12:00");

	@Test
	void 예약_시간_등록_성공() {
		ExtractableResponse<Response> response = 예약_시간_등록(request);

		assertThat(response.statusCode()).isEqualTo(200);
	}

	@Test
	void 예약_시간_형식이_다르면_실패() {
		ReservationTimeRequest requestWithWrongFormat = new ReservationTimeRequest("1200");
		ExtractableResponse<Response> response = 예약_시간_등록(requestWithWrongFormat);

		assertThat(response.statusCode()).isEqualTo(400);
	}

	@Test
	void 예약_시간_조회_성공() {
		예약_시간_등록(request);

		ExtractableResponse<Response> response = 예약_시간_조회();
		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.jsonPath().getList("id")).hasSize(1);
	}

	@Test
	void 예약_시간_삭제_성공() {
		예약_시간_등록(request);

		ExtractableResponse<Response> responseBeforeDelete = 예약_시간_조회();
		assertThat(responseBeforeDelete.statusCode()).isEqualTo(200);
		assertThat(responseBeforeDelete.jsonPath().getList("id")).hasSize(1);

		RestAssured.given().log().all()
				.when().delete("/times/1")
				.then().log().all()
				.statusCode(200);

		ExtractableResponse<Response> responseAfterDelete = 예약_시간_조회();
		assertThat(responseAfterDelete.statusCode()).isEqualTo(200);
		assertThat(responseAfterDelete.jsonPath().getList("id")).hasSize(0);
	}

	private ExtractableResponse<Response> 예약_시간_등록(ReservationTimeRequest request) {
		return RestAssured.given().log().all()
				.contentType(ContentType.JSON)
				.body(request)
				.when().post("/times")
				.then().log().all()
				.extract();
	}

	private ExtractableResponse<Response> 예약_시간_조회() {
		return RestAssured.given().log().all()
				.when().get("/times")
				.then().log().all()
				.extract();
	}
}
