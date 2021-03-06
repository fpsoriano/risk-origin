package com.origin.risk.controller;

import com.origin.risk.dto.request.UserInfoDto;
import com.origin.risk.dto.response.ProfileDto;
import com.origin.risk.service.InsuranceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/insurance")
public class InsuranceController {

    private final InsuranceService insuranceService;

    public InsuranceController(final InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @Operation(
            summary = "Determines the profile according to the user information",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Returns the risk profile."),
                    @ApiResponse(
                            responseCode = "405",
                            description = "Method not allowed (GET, DELETE, etc)",
                            content = @Content(schema = @Schema(implementation = Error.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = Error.class)))
            })
    @PostMapping("/profile")
    public ResponseEntity<ProfileDto> combate(
            @Valid @RequestBody final UserInfoDto userInfo) {
        var profile = insuranceService.riskProfile(userInfo.toUserInfo());
        return ResponseEntity.status(HttpStatus.OK).body(profile.toProfileDto());
    }
}
