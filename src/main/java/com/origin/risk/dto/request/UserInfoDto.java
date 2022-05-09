package com.origin.risk.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.origin.risk.dto.request.validator.ValueOfEnum;
import com.origin.risk.model.user.MaritalStatusEnum;
import com.origin.risk.model.user.UserInfo;
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

import static com.origin.risk.utils.constant.DtoConstant.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoDto {

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
    @NotBlank(message = REQUIRED_ERROR)
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
                .house(house != null ? house.toHouse() : null)
                .income(income)
                .marital_status(marital_status)
                .risk_questions(risk_questions)
                .vehicle(vehicle != null ? vehicle.toVehicle() : null)
                .build();
    }

}
