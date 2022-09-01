package utils;

import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.service.ExtentTestManager;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

/**
 * This is an implementation of the Filter interface provided by rest assured to ensure that the tests print the
 * response and the response into the report
 */
public class ExtentReportLogger implements Filter {
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        ExtentTestManager.getTest().info(MarkupHelper.createTable(new String[][]{{"Request URL", requestSpec.getURI()},
                {"Request body", requestSpec.getBody() == null ? "No Body Provided" : requestSpec.getBody().toString()},
                {"Header", requestSpec.getHeaders() == null ? "No headers present" :  requestSpec.getHeaders().toString()},
                {"Call Type", requestSpec.getMethod()}
        }));
        ExtentTestManager.getTest().info("Response");
        ExtentTestManager.getTest().info(MarkupHelper.createCodeBlock(response.asPrettyString(), CodeLanguage.JSON));
        ExtentTestManager.getTest().info(MarkupHelper.createLabel(String.valueOf(response.getStatusCode()), ExtentColor.GREEN));
        final int statusCode = response.statusCode();
        return response;
    }
}
