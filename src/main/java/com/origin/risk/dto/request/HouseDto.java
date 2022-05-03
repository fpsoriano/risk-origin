package com.origin.risk.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.origin.risk.dto.request.validator.ValueOfEnum;
import com.origin.risk.model.House;
import com.origin.risk.model.OwnershipStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class HouseDto {

    @ValueOfEnum(enumClass = OwnershipStatusEnum.class, message = "Valid values: owned | mortgaged")
    private String ownership_status;

    public House toHouse() {
        return House.builder()
                .ownership_status(ownership_status)
                .build();
    }
}
