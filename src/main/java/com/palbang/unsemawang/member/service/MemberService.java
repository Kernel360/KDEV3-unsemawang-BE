package com.palbang.unsemawang.member.service;

import com.palbang.unsemawang.member.dto.SignupExtraInfoDTO;
import com.palbang.unsemawang.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // nickname 중복 여부 확인
    public void duplicateNicknameCheck(String nickname) {
        // email로 디비에서 데이터 조회 -> 존재하면 중복 예외 발생 (탈퇴 시 개인정보 바로 삭제하므로 회원 상태 비교 불필요)
        if (!memberRepository.findByNickname(nickname).isEmpty()) {
            //throw new MemberAlreadyExistsException("이미 사용 중인 닉네임입니다.");
            throw new RuntimeException("이미 사용 중인 닉네임입니다.");
        }
    }

    //회원 추가 정보 입력
    public void signupExtraInfo(SignupExtraInfoDTO signupExtraInfoDTO) {
        //넘어온 데이터 유효성 체크(닉네임 중복 체크, 성별,생년월일, 태어난시간,양/음력, 전화번호
        //닉네임 중복체크 다시확인
        duplicateNicknameCheck(signupExtraInfoDTO.getNickname());

    }
}
