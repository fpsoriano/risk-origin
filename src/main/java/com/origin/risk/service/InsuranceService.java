package com.origin.risk.service;

import com.origin.risk.model.profile.Profile;
import com.origin.risk.model.user.UserInfo;
import org.springframework.stereotype.Service;

@Service
public interface InsuranceService {

    Profile riskProfile(UserInfo userInfo);
}
