package com.origin.risk.model.user;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum OwnershipStatusEnum {
    owned,
    mortgaged;

    public static OwnershipStatusEnum getType(String typeName) {
        return OwnershipStatusEnum.valueOf(typeName.toLowerCase());
    }
}
