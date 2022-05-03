package com.origin.risk.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.origin.risk.dto.request.validator.ValueOfEnum;
import com.origin.risk.model.MaritalStatusEnum;
import com.origin.risk.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    private int age;
    private int dependents;
    @Valid
    private HouseDto house;
    private int income;

    @ValueOfEnum(enumClass = MaritalStatusEnum.class, message = "Valid values: single | married")
    private String marital_status;
    private List<Integer> risk_questions;

    private VehicleDto vehicle;

    public UserInfo toUserInfo() {
        return UserInfo.builder()
                .age(age)
                .dependents(dependents)
                .house(house.toHouse())
                .income(income)
                .marital_status(marital_status)
                .risk_questions(risk_questions)
                .vehicle(vehicle.toVehicle())
                .build();
    }

}
