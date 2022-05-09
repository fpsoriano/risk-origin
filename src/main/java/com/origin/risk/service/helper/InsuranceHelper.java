package com.origin.risk.service.helper;

import com.origin.risk.model.InsuranceEnum;
import com.origin.risk.model.user.MaritalStatusEnum;
import com.origin.risk.model.user.OwnershipStatusEnum;
import com.origin.risk.model.user.UserInfo;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;

import static com.origin.risk.utils.constant.InsuranceConstant.*;

public class InsuranceHelper {

    /*
    * calculate the risk according to insurance
    * */
    public static String calculateRisk(final UserInfo userInfo, final InsuranceEnum insurance, final int riskScore) {

        var score = riskScore;

        // validations that are common for all insurance
        score += isUserUnder30yearsOld(userInfo) ? -2 : isUserAgeBetween30and40(userInfo) ? -1 : 0;
        score += isUserIncomeAbove200k(userInfo) ? -1 : 0;

        switch (insurance){
            case AUTO:
                if (!doesUserHaveVehicle(userInfo))
                    return INELIGIBLE;
                score += isUsersVehicleProducedInTheLast5Years(userInfo) ? 1 : 0;
                break;

            case HOME:
                if (!doesUserHaveHouse(userInfo))
                    return INELIGIBLE;
                score += isUsersHouseMortgaged(userInfo) ? 1 : 0;
                break;

            case LIFE:
                if (isUserOver60yearsOld(userInfo))
                    return INELIGIBLE;
                score += doesUserHasDependents(userInfo) ? 1 : 0;
                score += isUserMarried(userInfo) ? 1 : 0;
                break;

            case DISABILITY:
                if (!doesUserHaveIncome(userInfo) || (isUserOver60yearsOld(userInfo)))
                    return INELIGIBLE;
                score += isUsersHouseMortgaged(userInfo) ? 1 : 0;
                score += doesUserHasDependents(userInfo) ? 1 : 0;
                score += isUserMarried(userInfo) ? -1 : 0;
                break;
        }
        return returnProfileDescriptionAccordingToScore(score);
    }

    private static String returnProfileDescriptionAccordingToScore(final int score) {
        return (score < 1) ? ECONOMIC : (score < 3) ? REGULAR : RESPONSIBLE;
    }

    private static boolean doesUserHaveIncome(final UserInfo userInfo){
        if (userInfo.getIncome() > 0)
            return true;
        return false;
    }

    private static boolean doesUserHaveVehicle(final UserInfo userInfo){
        if (ObjectUtils.isEmpty(userInfo.getVehicle()))
            return false;
        return true;
    }

    private static boolean doesUserHaveHouse(final UserInfo userInfo){
        if (ObjectUtils.isEmpty(userInfo.getHouse()))
            return false;
        return true;
    }

    private static boolean isUserOver60yearsOld(final UserInfo userInfo){
        if (userInfo.getAge() > 60)
            return true;
        return false;
    }

    private static boolean isUserUnder30yearsOld(final UserInfo userInfo){
        if (userInfo.getAge() < 30)
            return true;
        return false;
    }

    private static boolean isUserAgeBetween30and40(final UserInfo userInfo){
        if (userInfo.getAge() >= 30 && userInfo.getAge() <= 40)
            return true;
        return false;
    }

    private static boolean isUserIncomeAbove200k(final UserInfo userInfo){
        if (userInfo.getIncome() > 200000)
            return true;
        return false;
    }

    private static boolean isUsersHouseMortgaged(final UserInfo userInfo){
        if (userInfo.getHouse() != null && userInfo.getHouse().getOwnership_status().equals(OwnershipStatusEnum.mortgaged.name()))
            return true;
        return false;
    }

    private static boolean doesUserHasDependents(final UserInfo userInfo){
        if (userInfo.getDependents() > 0)
            return true;
        return false;
    }

    private static boolean isUserMarried(final UserInfo userInfo){
        if (userInfo.getMarital_status().equals(MaritalStatusEnum.married.name()))
            return true;
        return false;
    }

    private static boolean isUsersVehicleProducedInTheLast5Years(final UserInfo userInfo){
        var currentYear = LocalDate.now().getYear();
        if ( currentYear - userInfo.getVehicle().getYear() < 6)
            return true;
        return false;
    }

}
