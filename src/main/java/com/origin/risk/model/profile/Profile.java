package com.origin.risk.model.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.origin.risk.dto.response.ProfileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    private String auto;
    private String disability;
    private String home;
    private String life;

    public ProfileDto toProfileDto() {
        return ProfileDto.builder()
                .auto(auto)
                .disability(disability)
                .home(home)
                .life(life)
                .build();
    }
}
