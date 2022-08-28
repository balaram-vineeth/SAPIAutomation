import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import utils.ExtentReportLogger;

public class Sender {

    public static RequestSpecification createRequestSpecifications() {
        return RestAssured.given()
                .contentType("application/json")
//                .config(CurlRestAssuredConfigFactory.createConfig())
                .log()
                .all()
                .filter(new ExtentReportLogger());
    }


}
