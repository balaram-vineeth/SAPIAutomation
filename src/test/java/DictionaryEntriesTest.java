import com.aventstack.extentreports.service.ExtentTestManager;
import com.aventstack.extentreports.testng.listener.ExtentITestListenerAdapter;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.GetData;
import utils.Sender;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Listeners({ExtentITestListenerAdapter.class})
public class DictionaryEntriesTest {

    /**
     * Setup the rest assured instance with the correct baseURL and the API path
     */
    @BeforeClass
    private void setUp() {
        Properties properties = loadProperties();
        RestAssured.baseURI = properties.getProperty("baseURL");
        RestAssured.basePath = properties.getProperty("entriesURL");
    }

    /**
     * Test that the API is able to retrieve the result for a word that exists
     */
    @Test
    private void shouldBeAbleToGetWordEntry() {
        Sender.createRequestSpecifications().get("resource").then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    /**
     * Testng the handling for non english words
     */
    @Test
    private void shouldFailWhenPassingNonEnglishWords() {
        Sender.createRequestSpecifications().get("buchen").then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    /**
     * Test to verify API schema for non-existent words
     */
    @Test
    private void shouldProvideCorrectSchemaForNotFound() {
        Sender.createRequestSpecifications().get("buchen").then().assertThat().body(JsonSchemaValidator
                .matchesJsonSchema(GetData.getFileAsString("src/test/resources/schema/notFoundSchema.json")));
    }

    /**
     * Test to see if the API responses are containing valid URLs
     */
    @Test
    private void shouldVerifyThatAllURLsInResponseAreValid() {
        JsonPath resource = Sender.createRequestSpecifications().get("resource").jsonPath();
        List<String> phoneticsURL = (List) resource.getList("phonetics.sourceUrl").get(0);
        List<String> phoneticsLicenceURL = (List) resource.getList("phonetics.license.url").get(0);
        phoneticsURL.forEach(url -> Assert.assertTrue(assertURLsAreValid(url)));
        phoneticsLicenceURL.forEach(url -> Assert.assertTrue(assertURLsAreValid(url)));
    }

    @DataProvider(name = "charSet")
    private Object[][] getNonStandardCharSet() {
        return new Object[][] {
            {"知道"},
            {"Köln"}
        };
    }

    /**
     * A data driven test to see how the API handles non-standard characters
     * @param nonStdVerbs
     */
    @Test(dataProvider = "charSet")
    private void verifyIfTheAPIHandlesNonStandardCharacters(String nonStdVerbs) {
        Sender.createRequestSpecifications().get(nonStdVerbs).then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND);
    }


    private boolean assertURLsAreValid(String url) {
        UrlValidator urlValidator = new UrlValidator();
        if (urlValidator.isValid(url)) {
            ExtentTestManager.getTest().pass("The URL " + url + " is valid");
            return true;
        } else {
            ExtentTestManager.getTest().fail("The URL `" + url + " is not valid");
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
