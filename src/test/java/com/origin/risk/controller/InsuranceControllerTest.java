package com.origin.risk.controller;

import com.google.gson.Gson;
import com.origin.risk.dto.request.UserInfoDto;
import com.origin.risk.dto.response.ProfileDto;
import com.origin.risk.exception.Error;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ExecutionException;

import static com.origin.risk.exception.ErrorCodes.MALFORMED_REQUEST;
import static com.origin.risk.exception.ErrorCodes.METHOD_NOT_ALLOWED;
import static com.origin.risk.helper.InsuranceHelperTest.mockUserInfoDto;
import static com.origin.risk.utils.constant.InsuranceConstant.RESPONSIBLE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InsuranceControllerTest {

    private static final String PATH = "/insurance/profile";

    @Autowired
    private TestRestTemplate testRestTemplate;

    private Gson mapper = new Gson();

    @Test
    @DisplayName("Risk Profile: Success")
    void riskProfile_success() throws ExecutionException, InterruptedException {
        // GIVEN
        UserInfoDto mockUserInfoDto = mockUserInfoDto();

        // WHEN
        ResponseEntity<String> response =
                testRestTemplate.postForEntity(PATH, mockUserInfoDto, String.class);
        ProfileDto profileDto = mapper.fromJson(response.getBody(), ProfileDto.class);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(profileDto.getAuto()).isEqualTo(RESPONSIBLE);
        assertThat(profileDto.getDisability()).isEqualTo(RESPONSIBLE);
        assertThat(profileDto.getHome()).isEqualTo(RESPONSIBLE);
        assertThat(profileDto.getLife()).isEqualTo(RESPONSIBLE);

    }

    @Test
    @DisplayName("Risk Profile: Required Fields Validation ")
    void riskProfile_requiredFieldsValidation() throws ExecutionException, InterruptedException {
        // GIVEN
        UserInfoDto mockUserInfoDto = new UserInfoDto();

        // WHEN
        ResponseEntity<String> response =
                testRestTemplate.postForEntity(PATH, mockUserInfoDto, String.class);
        Error error = mapper.fromJson(response.getBody(), Error.class);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(error.getCode()).isEqualTo(MALFORMED_REQUEST.getCode());
        assertThat(error.getMessage()).isEqualTo(MALFORMED_REQUEST.getMessage());

        // validation error fields
        assertThat(error.getDetails().contains("age: is a required field"));
        assertThat(error.getDetails().contains("income: is a required field"));
        assertThat(error.getDetails().contains("marital_status: is a required field\""));
        assertThat(error.getDetails().contains("risk_questions: is a required field"));
        assertThat(error.getDetails().contains("dependents: is a required field"));

    }

    @Test
    @DisplayName("Risk Profile: Input Validation Error")
    void riskProfile_inputValidationError() throws ExecutionException, InterruptedException {
        // GIVEN
        String mockUserInfoJson = "{\n" +
                "  \"age\": -1,\n" +
                "  \"dependents\": -1,\n" +
                "  \"house\": {\n" +
                "    \"ownership_status\": \"invalid\"\n" +
                "  },\n" +
                "  \"income\": -1,\n" +
                "  \"marital_status\": \"invalid\",\n" +
                "  \"risk_questions\": [\n" +
                "    2,\n" +
                "    2,\n" +
                "    2,\n" +
                "    2\n" +
                "  ],\n" +
                "  \"vehicle\": {\n" +
                "    \"year\": 0\n" +
                "  }\n" +
                "}";
        UserInfoDto userInfoDto = mapper.fromJson(mockUserInfoJson, UserInfoDto.class);

        // WHEN
        ResponseEntity<String> response =
                testRestTemplate.postForEntity(PATH, userInfoDto, String.class);
        Error error = mapper.fromJson(response.getBody(), Error.class);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(error.getCode()).isEqualTo(MALFORMED_REQUEST.getCode());
        assertThat(error.getMessage()).isEqualTo(MALFORMED_REQUEST.getMessage());

        // risk_questions field validation
        assertThat(error.getDetails().contains("risk_questions: Risk questions must have 3 answers"));
        assertThat(error.getDetails().contains("risk_questions[0]: The value must be between 0 and 1"));
        assertThat(error.getDetails().contains("risk_questions[1]: The value must be between 0 and 1"));
        assertThat(error.getDetails().contains("risk_questions[2]: The value must be between 0 and 1"));

        // number fields validation
        assertThat(error.getDetails().contains("vehicle.year: must be greater than 0"));
        assertThat(error.getDetails().contains("age: equal or greater than 0"));
        assertThat(error.getDetails().contains("income: equal or greater than 0"));
        assertThat(error.getDetails().contains("dependents: equal or greater than 0"));

        // enum validation
        assertThat(error.getDetails().contains("marital_status: Invalid value"));
        assertThat(error.getDetails().contains("house.ownership_status: Invalid value"));
    }

    @Test
    @DisplayName("Risk Profile: method not allowed")
    void riskProfile_methodNotAllowed() throws ExecutionException, InterruptedException {

        // WHEN
        ResponseEntity<String> response =
                testRestTemplate.exchange(PATH, HttpMethod.PUT, null, String.class);
        Error error = mapper.fromJson(response.getBody(), Error.class);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(error.getCode()).isEqualTo(METHOD_NOT_ALLOWED.getCode());
        assertThat(error.getMessage()).isEqualTo(METHOD_NOT_ALLOWED.getMessage());
    }

}