package com.palbang.unsemawang.member.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupExtraInfoDTO {
    private String id;
    private String nickname;
    private char sex;
    private int year;
    private int month;
    private int day;
    private int hour;
    private String solunar; // "solar", "lunar"
    private int youn; //윤달여부?
    private String phone;
}
