package nana.TrialTrove.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nana.TrialTrove.domain.MemberDTO;
import nana.TrialTrove.domain.MemberEntity;
import nana.TrialTrove.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@Service
@RequiredArgsConstructor
public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Transactional
    public MemberDTO joinMember(@Valid MemberDTO memberDTO) {
        log.info("입력된 비밀번호: {}",memberDTO.getUserPw());
        // 패스워드 유효성 검사
        if (!isValidPassword(memberDTO.getUserPw())) {
            throw new IllegalArgumentException("비밀번호는 숫자, 영문자, 특수문자를 포함해야합니다");
        }
        // 패스워드 암호화
        String encryptedPassword = passwordEncoder.encode(memberDTO.getUserPw());
        // 암호화된 비밀번호 앞에 알고리즘 정보를 추가하여 저장
        // String passwordWithAlgorithm = "{bcrypt}" + encryptedPassword;


        // DTO를 엔티티로 변환
        MemberEntity memberEntity = modelMapper.map(memberDTO, MemberEntity.class);
        memberEntity.setUserPw(encryptedPassword);

        // 회원 저장
        memberRepository.save(memberEntity);

        return modelMapper.map(memberEntity, MemberDTO.class);
    }

    // 아이디 중복체크
    public boolean checkId(String userId) {
       return memberRepository.findByUserId(userId).isPresent();
    }

    public MemberEntity findMemberByUserId(String userId) {
        return memberRepository.findByUserId(userId).orElse(null);
    }

    // 비밀번호 유효성 검사
    private boolean isValidPassword(String userPw) {
        // 비밀번호가 숫자, 영문자, 특수문자를 모두 포함하는지 확인하는 정규 표현식
        String regex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).{8,20}$";
        // 정규 표현식을 이용하여 비밀번호 검사
        return userPw.matches(regex);
    }


}

