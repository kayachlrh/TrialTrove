package nana.TrialTrove.service;

import nana.TrialTrove.domain.MemberEntity;
import nana.TrialTrove.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final MemberRepository memberRepository;
    //private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    public boolean login(String userId, String userPw) {
        // 사용자 아이디로 사용자 정보 조회
        Optional<MemberEntity> userOptional = memberRepository.findByUserId(userId);
//        if (member != null) {
//            // 회원 정보가 존재하면 입력된 비밀번호를 검증합니다.
//            return passwordEncoder.matches(userPw, member.getUserPw());
//        }
        // 사용자 정보가 존재하고 비밀번호가 일치하는지 확인
        return userOptional.isPresent() && userOptional.get().getUserPw().equals(userPw);
    }

}
