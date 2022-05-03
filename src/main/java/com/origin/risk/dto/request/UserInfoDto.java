package com.origin.risk.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.origin.risk.dto.request.validator.ValueOfEnum;
import com.origin.risk.model.MaritalStatusEnum;
import com.origin.risk.model.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoDto {

    private final static String POSITIVE_OR_ZERO_ERROR = "equal or greater than 0";
    private final static String REQUIRED_ERROR = "is a required field";

    private final static String POSITIVE_OR_ZERO_DESCRIPTION = "An integer equal or greater than 0";

    @NotNull(message = REQUIRED_ERROR)
    @PositiveOrZero(message = POSITIVE_OR_ZERO_ERROR)
    @Schema(description = POSITIVE_OR_ZERO_DESCRIPTION)
    private Integer age;

    @NotNull(message = REQUIRED_ERROR)
    @PositiveOrZero(message = POSITIVE_OR_ZERO_ERROR)
    @Schema(description = POSITIVE_OR_ZERO_DESCRIPTION)
    private Integer dependents;

    @Valid
    private HouseDto house;

    @NotNull(message = REQUIRED_ERROR)
    @PositiveOrZero(message = POSITIVE_OR_ZERO_ERROR)
    @Schema(description = POSITIVE_OR_ZERO_DESCRIPTION)
    private Integer income;

    @ValueOfEnum(enumClass = MaritalStatusEnum.class)
    @NotBlank
    @Schema(type = "String", allowableValues = { "single", "married" })
    private String marital_status;

    @NotNull(message = REQUIRED_ERROR)
    @Size(min=3, max=3, message = "Risk questions must have 3 answers")
    @Schema(description = "An array with 3 booleans: 0 or 1")
    private List<@Range(min = 0, max = 1, message = "The value must be between 0 and 1") Integer> risk_questions;

    @Valid
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
