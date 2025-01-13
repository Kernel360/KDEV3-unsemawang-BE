package com.palbang.unsemawang.member.controller;

import com.palbang.unsemawang.member.dto.SignupExtraInfoDTO;
import com.palbang.unsemawang.member.service.MemberService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;


    //닉네임 중복 체크
    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, String>> checkNickname(
            @Pattern(
                    regexp = "^[A-Za-z\\d가-힣]{2,10}$",
                    message = "닉네임은 2~10자 이내의 문자(한글, 영어, 숫자)여야 합니다."
            )
            String nickname) {

        memberService.duplicateNicknameCheck(nickname);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "사용 가능한 닉네임입니다."
        ));
    }

    //회원가입 추가정보 입력
    @PostMapping("/signup/extra-info")
    public ResponseEntity<Map<String, String>> signupExtraInfo(@RequestBody SignupExtraInfoDTO signupExtraInfoDTO) {

        memberService.signupExtraInfo(signupExtraInfoDTO);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "회원가입 완료."
        ));
    }

}