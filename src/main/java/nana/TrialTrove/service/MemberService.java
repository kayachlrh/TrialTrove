package nana.TrialTrove.service;

import nana.TrialTrove.domain.MemberEntity;
import nana.TrialTrove.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberEntity joinMember(String userId, String userPw, String email, String name) {
        // 패스워드 암호화
        String encryptedPassword = passwordEncoder.encode(userPw);

        // 회원 엔티티 생성
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUserId(userId);
        memberEntity.setUserPw(encryptedPassword);
        memberEntity.setEmail(email);
        memberEntity.setName(name);

        // 회원 저장
        return memberRepository.save(memberEntity);
    }

    public MemberEntity findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }

    public MemberEntity findMemberByUserId(String userId) {
        return memberRepository.findByUserId(userId).orElse(null);
    }
}

