package com.origin.risk.service.impl;

import com.origin.risk.model.InsuranceEnum;
import com.origin.risk.model.profile.Profile;
import com.origin.risk.model.user.UserInfo;
import com.origin.risk.service.InsuranceService;
import org.springframework.stereotype.Service;

import static com.origin.risk.service.helper.InsuranceHelper.calculateRisk;

@Service
class InsuranceServiceImpl implements InsuranceService {

    @Override
    public Profile riskProfile(final UserInfo userInfo) {
        var riskScore = userInfo.getRisk_questions().stream().mapToInt(Integer::intValue).sum();
        var profile = Profile.builder()
                        .auto(calculateRisk(userInfo, InsuranceEnum.AUTO, riskScore))
                        .disability(calculateRisk(userInfo, InsuranceEnum.DISABILITY, riskScore))
                        .home(calculateRisk(userInfo, InsuranceEnum.HOME, riskScore))
                        .life(calculateRisk(userInfo, InsuranceEnum.LIFE, riskScore))
                        .build();
        return profile;
    }
}
