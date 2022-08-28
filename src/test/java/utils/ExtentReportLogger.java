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

public class ExtentReportLogger implements Filter {
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        ExtentTestManager.getTest().info(MarkupHelper.createCodeBlock(response.asPrettyString(), CodeLanguage.JSON));
        ExtentTestManager.getTest().info(MarkupHelper.createLabel(String.valueOf(response.getStatusCode()), ExtentColor.GREEN));
        final int statusCode = response.statusCode();
        return response;
    }
}
