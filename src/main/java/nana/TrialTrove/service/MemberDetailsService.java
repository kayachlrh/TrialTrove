package nana.TrialTrove.service;

import lombok.RequiredArgsConstructor;
import nana.TrialTrove.domain.MemberEntity;
import nana.TrialTrove.domain.UserRole;
import nana.TrialTrove.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private static final Logger log = LoggerFactory.getLogger(MemberDetailsService.class);


    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.trace("사용자 로그인 시도: {}", userId);
        MemberEntity member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    String errorMessage = "사용자를 찾을 수 없습니다: " + userId;
                    log.error(errorMessage); // 사용자를 찾을 수 없는 경우 에러 레벨로 로깅합니다.
                    return new UsernameNotFoundException(errorMessage);
                });

        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(userId)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
            log.info("사용자 {}에게 ADMIN 권한이 부여되었습니다.", userId);
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
            log.info("사용자 {}에게 USER 권한이 부여되었습니다.", userId);
        }
        log.info("사용자 {}가 성공적으로 로그인하였습니다.", userId);
        return new User(member.getUserId(), member.getUserPw(), authorities);
    }
}
