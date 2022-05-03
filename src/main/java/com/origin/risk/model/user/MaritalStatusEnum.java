package com.origin.risk.model.user;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum MaritalStatusEnum {
    single,
    married;

    public static MaritalStatusEnum getType(String typeName) {
        return MaritalStatusEnum.valueOf(typeName.toLowerCase());
    }
}
