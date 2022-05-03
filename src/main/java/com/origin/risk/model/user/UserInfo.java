package com.origin.risk.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private int age;
    private int dependents;
    private House house;
    private int income;
    private String marital_status;
    private List<Integer> risk_questions;
    private Vehicle vehicle;
}
