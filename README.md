# API Automation Demo

This repo contains a few tests for `https://api.dictionaryapi.dev/api`

The test use Rest Assured as the testing library and TestNG as the testing framework. 
The reporting is handled by Extent Reports, the report is saved in `test-output` folder.

The Test data is externalised in the file testData.properties

## Running the tests
### Running locally
The test can be run by running the command `mvn test` from the root directory.

### Running in GitHub
To run the test in GitHub, the pipeline is configured as an on demand pipeline. Follow the below steps to run the pipeline
1. Navigate to the Actions menu
2. Select the workflow named `Run API test`
3. Click on the Run Workflow option
4. The branch is selected as main by default, click om Run workflow
5. Once the test is complete the report can be downloaded from the Artifacts section

## Tests Explained 
The tests can be found in the class `DictionaryEntriesTest`

Here the following tests are covered

* `shouldBeAbleToGetWordEntry` - This is a positive check to see that the API is able to retrieve the meaning of a work, no detailed checks are added here we just verify that the status code is 200
* `shouldFailWhenPassingNonEnglishWords` - This is a check to see if the test fails for a non english word, the verification here is to check that the API returns 404. 
* `shouldProvideCorrectSchemaForNotFound` - This is to check that the API provides the response in the correct schema if the word is not found, here we use RestAssured's schema validator to accomplish this. 
* `shouldVerifyThatAllURLsInResponseAreValid` - This is to verify that the URL's provided by the API response is valid ones, we verify that the url's are valid with `UrlValidator` class from Apache Commons
* `verifyIfTheAPIHandlesNonStandardCharacters` This to a data driven test to verify how the API handles non-standard character set, here we pass a Chinese and german word to see how the API handles these  
