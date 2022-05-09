package com.origin.risk.helper;

import com.origin.risk.dto.request.HouseDto;
import com.origin.risk.dto.request.UserInfoDto;
import com.origin.risk.dto.request.VehicleDto;
import com.origin.risk.model.user.UserInfo;

import java.util.Arrays;

public class InsuranceHelperTest {

    public static UserInfo mockUserInfo() {
        return mockUserInfoDto().toUserInfo();
    }

    public static UserInfoDto mockUserInfoDto() {
        return UserInfoDto.builder()
                .age(41)
                .dependents(0)
                .house(new HouseDto("owned"))
                .income(200000)
                .marital_status("single")
                .risk_questions(Arrays.asList(1,1,1))
                .vehicle(new VehicleDto(2010))
                .build();
    }
}
