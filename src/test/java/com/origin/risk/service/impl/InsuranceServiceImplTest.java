package com.origin.risk.service.impl;

import com.origin.risk.model.profile.Profile;
import com.origin.risk.model.user.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;

import static com.origin.risk.helper.InsuranceHelperTest.mockUserInfo;
import static com.origin.risk.utils.constant.InsuranceConstant.*;

@ExtendWith(MockitoExtension.class)
class InsuranceServiceImplTest {

    @InjectMocks
    private InsuranceServiceImpl insuranceService;

    @Test
    @DisplayName("If the user doesn’t have income, she is ineligible for disability")
    void insuranceDisability_ineligible_noIncome() {
        // GIVEN user does not have income
        UserInfo userInfo = mockUserInfo();
        userInfo.setIncome(0);

        // WHEN insurance risk profile calculated
        Profile profile = insuranceService.riskProfile(userInfo);

        // THEN user is ineligible
        Assertions.assertEquals(INELIGIBLE, profile.getDisability());
    }

    @Test
    @DisplayName("If the user is over 60 years old, she is ineligible for disability insurance")
    void insuranceDisability_ineligible_ageOver60YearsOld() {
        // GIVEN user with age over then 60 years old
        UserInfo userInfo = mockUserInfo();
        userInfo.setAge(61);

        // WHEN insurance risk profile calculated
        Profile profile = insuranceService.riskProfile(userInfo);

        // THEN user is ineligible
        Assertions.assertEquals(INELIGIBLE, profile.getDisability());
    }

    @Test
    @DisplayName("If the user doesn’t have vehicles, she is ineligible for auto")
    void insuranceAuto_ineligible_noVehicles() {
        // GIVEN user does not have vehicles
        UserInfo userInfo = mockUserInfo();
        userInfo.setVehicle(null);

        // WHEN insurance risk profile calculated
        Profile profile = insuranceService.riskProfile(userInfo);

        // THEN user is ineligible
        Assertions.assertEquals(INELIGIBLE, profile.getAuto());
    }

    @Test
    @DisplayName("If the user doesn’t have houses, she is ineligible for home insurance")
    void insuranceHome_ineligible_noHouses() {
        // GIVEN user does not have houses
        UserInfo userInfo = mockUserInfo();
        userInfo.setHouse(null);

        // WHEN insurance risk profile calculated
        Profile profile = insuranceService.riskProfile(userInfo);

        // THEN user is ineligible
        Assertions.assertEquals(INELIGIBLE, profile.getHome());
    }

    @Test
    @DisplayName("If the user is over 60 years old, she is ineligible for life insurance")
    void insuranceLife_ineligible_ageOver60YearsOld() {
        // GIVEN user with age over then 60 years old
        UserInfo userInfo = mockUserInfo();
        userInfo.setAge(61);

        // WHEN insurance risk profile calculated
        Profile profile = insuranceService.riskProfile(userInfo);

        // THEN user is ineligible
        Assertions.assertEquals(INELIGIBLE, profile.getLife());
    }

    @Test
    @DisplayName("If the user is between 30 and 40 years old, deduct 1 from all lines of insurance")
    void insurance_ageBetween30And40() {
        // GIVEN user with age between 30 and 40 years old
        UserInfo userInfo = mockUserInfo();
        userInfo.setAge(31);

        // WHEN insurance risk profile calculated
        Profile profile = insuranceService.riskProfile(userInfo);

        // THEN all insurances should be REGULAR = risk_questions(1 + 1 + 1) - ageBetween30And40(-1) = 2 REGULAR
        Assertions.assertEquals(REGULAR, profile.getLife());
        Assertions.assertEquals(REGULAR, profile.getAuto());
        Assertions.assertEquals(REGULAR, profile.getHome());
        Assertions.assertEquals(REGULAR, profile.getDisability());
    }

    @Test
    @DisplayName("If the user is under 30 years old, deduct 2 risk points from all lines of insurance")
    void insurance_ageUnder30() {
        // GIVEN user with age under 30 years old
        UserInfo userInfo = mockUserInfo();
        userInfo.setAge(20);
        userInfo.setRisk_questions(Arrays.asList(1,1,0));

        // WHEN insurance risk profile calculated
        Profile profile = insuranceService.riskProfile(userInfo);

        // THEN all insurances should be ECONOMIC = risk_questions(1 + 1 + 0) - ageBetween30And40(-2) = 0 REGULAR
        Assertions.assertEquals(ECONOMIC, profile.getLife());
        Assertions.assertEquals(ECONOMIC, profile.getAuto());
        Assertions.assertEquals(ECONOMIC, profile.getHome());
        Assertions.assertEquals(ECONOMIC, profile.getDisability());
    }

    @Test
    @DisplayName("If the user's income is above $200k, deduct 1 risk point from all lines of insurance")
    void insurance_incomeAbove200K() {
        // GIVEN user with income above $200k
        UserInfo userInfo = mockUserInfo();
        userInfo.setIncome(200001);

        // WHEN insurance risk profile calculated
        Profile profile = insuranceService.riskProfile(userInfo);

        // THEN all insurances should be REGULAR = risk_questions(1 + 1 + 1) - incomeAbove200K(-1) = 2 REGULAR
        Assertions.assertEquals(REGULAR, profile.getLife());
        Assertions.assertEquals(REGULAR, profile.getAuto());
        Assertions.assertEquals(REGULAR, profile.getHome());
        Assertions.assertEquals(REGULAR, profile.getDisability());
    }

    @Test
    @DisplayName("If the user's house is mortgaged, add 1 risk point to her home score and add 1 risk point to her disability score")
    void insurance_houseMortgaged() {
        // GIVEN user's house is mortgaged
        UserInfo userInfo = mockUserInfo();
        userInfo.setRisk_questions(Arrays.asList(1,1,0));
        userInfo.getHouse().setOwnership_status("mortgaged");

        // WHEN insurance risk profile calculated
        Profile profile = insuranceService.riskProfile(userInfo);

        // THEN risk_questions(1 + 1 + 0) + houseMortgaged(+1home +1disability)
        Assertions.assertEquals(REGULAR, profile.getLife());
        Assertions.assertEquals(REGULAR, profile.getAuto());
        Assertions.assertEquals(RESPONSIBLE, profile.getHome());
        Assertions.assertEquals(RESPONSIBLE, profile.getDisability());
    }

    @Test
    @DisplayName("If the user has dependents, add 1 risk point to both the disability and life scores.")
    void insurance_userWithDependents() {
        // GIVEN user with dependents
        UserInfo userInfo = mockUserInfo();
        userInfo.setRisk_questions(Arrays.asList(1,1,0));
        userInfo.setDependents(1);

        // WHEN insurance risk profile calculated
        Profile profile = insuranceService.riskProfile(userInfo);

        // THEN risk_questions(1 + 1 + 0) + userWithDependents(+1life +1disability)
        Assertions.assertEquals(RESPONSIBLE, profile.getLife());
        Assertions.assertEquals(REGULAR, profile.getAuto());
        Assertions.assertEquals(REGULAR, profile.getHome());
        Assertions.assertEquals(RESPONSIBLE, profile.getDisability());
    }

    @Test
    @DisplayName("If the user is married, add 1 risk point to the life score and remove 1 risk point from disability.")
    void insurance_userMarried() {
        // GIVEN married user
        UserInfo userInfo = mockUserInfo();
        userInfo.setMarital_status("married");

        // WHEN insurance risk profile calculated
        Profile profile = insuranceService.riskProfile(userInfo);

        // THEN risk_questions(1 + 1 + 1) + userMarried(+1life -1disability)
        Assertions.assertEquals(RESPONSIBLE, profile.getLife());
        Assertions.assertEquals(RESPONSIBLE, profile.getAuto());
        Assertions.assertEquals(RESPONSIBLE, profile.getHome());
        Assertions.assertEquals(REGULAR, profile.getDisability());
    }

    @Test
    @DisplayName("If the user's vehicle was produced in the last 5 years, add 1 risk point to that vehicle’s score.")
    void insurance_userVehicleProducedInTheLast5Years() {
        // GIVEN user's vehicle was produced in the last 5 years
        UserInfo userInfo = mockUserInfo();
        var vehicleYear = LocalDate.now().getYear() - 2;
        userInfo.getVehicle().setYear(vehicleYear);
        userInfo.setRisk_questions(Arrays.asList(1,1,0));

        // WHEN insurance risk profile calculated
        Profile profile = insuranceService.riskProfile(userInfo);

        // THEN risk_questions(1 + 1 + 0) + userVehicleProducedInTheLast5Years(+1auto)
        Assertions.assertEquals(REGULAR, profile.getLife());
        Assertions.assertEquals(RESPONSIBLE, profile.getAuto());
        Assertions.assertEquals(REGULAR, profile.getHome());
        Assertions.assertEquals(REGULAR, profile.getDisability());
    }

}