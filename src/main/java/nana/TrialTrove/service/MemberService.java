package nana.TrialTrove.service;

import lombok.RequiredArgsConstructor;
import nana.TrialTrove.domain.MemberDTO;
import nana.TrialTrove.domain.MemberEntity;
import nana.TrialTrove.repository.MemberRepository;
import org.modelmapper.ModelMapper;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    //private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Transactional
    public MemberDTO joinMember(MemberDTO memberDTO) {
        // 패스워드 암호화
       // String encryptedPassword = passwordEncoder.encode(memberDTO.getUserPw());

        // DTO를 엔티티로 변환
        MemberEntity memberEntity = modelMapper.map(memberDTO, MemberEntity.class);
        //memberEntity.setUserPw(encryptedPassword);

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

    public boolean login(String userId, String userPw) {
        Optional<MemberEntity> memberOptional = memberRepository.findByUserId(userId);
        if (memberOptional.isPresent()) {
            MemberEntity member = memberOptional.get();
            // 비밀번호 검증
            //return passwordEncoder.matches(userPw, member.getUserPw());
        }
        return false;
    }
}

