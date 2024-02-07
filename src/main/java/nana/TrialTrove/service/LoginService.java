package nana.TrialTrove.service;

import nana.TrialTrove.domain.MemberEntity;
import nana.TrialTrove.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean login(String userId, String userPw) {
        // 입력된 아이디로 회원 정보를 조회합니다.
        MemberEntity member = memberRepository.findByUserId(userId).orElse(null);
        if (member != null) {
            // 회원 정보가 존재하면 입력된 비밀번호를 검증합니다.
            return passwordEncoder.matches(userPw, member.getUserPw());
        }
        return false; // 회원 정보가 없는 경우 로그인 실패
    }
}
