package utils;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class Sender {

    public static RequestSpecification createRequestSpecifications() {
        return RestAssured.given()
                .contentType("application/json")
                .log()
                .all()
                .filter(new ExtentReportLogger());
    }


}
