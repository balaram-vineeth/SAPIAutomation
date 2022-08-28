import com.aventstack.extentreports.service.ExtentTestManager;
import com.aventstack.extentreports.testng.listener.ExtentITestListenerAdapter;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.GetData;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Listeners({ExtentITestListenerAdapter.class})
public class DictionaryEntriesTest {

    @BeforeClass
    private void setUp() {
        Properties properties = loadProperties();
        RestAssured.baseURI = properties.getProperty("baseURL");
        RestAssured.basePath = properties.getProperty("entriesURL");
    }

    @Test
    private void shouldBeAbleToGetWordEntry() {
        Sender.createRequestSpecifications().get("resource").then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test
    private void shouldFailWhenPassingNonEnglishWords() {
        Sender.createRequestSpecifications().get("buchen").then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    private void shouldProvideCorrectSchemaForNotFound() {
        Sender.createRequestSpecifications().get("buchen").then().assertThat().body(JsonSchemaValidator
                .matchesJsonSchema(GetData.getFileAsString("src/test/resources/schema/notFoundSchema.json")));
    }

    @Test
    private void shouldVerifyThatAllURLsInResponseAreValid() {
        JsonPath resource = Sender.createRequestSpecifications().get("resource").jsonPath();
        List<String> phoneticsURL = (List) resource.getList("phonetics.sourceUrl").get(0);
        List<String> phoneticsLicenceURL = (List) resource.getList("phonetics.license.url").get(0);
        phoneticsURL.forEach(url -> Assert.assertTrue(assertURLsAreValid(url)));
        phoneticsLicenceURL.forEach(url -> Assert.assertTrue(assertURLsAreValid(url)));
    }

    private boolean assertURLsAreValid(String url) {
        UrlValidator urlValidator = new UrlValidator();
        if (urlValidator.isValid(url)) {
            ExtentTestManager.getTest().pass("The URL + " + url + " is valid");
            return true;
        } else {
            ExtentTestManager.getTest().fail("The URL + " + url + " is not valid");
            return false;
        }
    }


    private Properties loadProperties() {
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream("src/test/resources/testData.properties"));
            return prop;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
