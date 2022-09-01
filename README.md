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