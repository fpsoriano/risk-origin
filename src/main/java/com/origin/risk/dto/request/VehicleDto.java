package com.origin.risk.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.origin.risk.model.Vehicle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {

    @Positive(message = "must be greater than 0")
    @Schema(description = "A positive integer corresponding to the year it was manufactured", defaultValue = "2010")
    private int year;

    public Vehicle toVehicle() {
        return Vehicle.builder()
                .year(year)
                .build();
    }
}
