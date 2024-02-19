//package nana.TrialTrove.service;
//
//import lombok.RequiredArgsConstructor;
//import nana.TrialTrove.domain.MemberEntity;
//import nana.TrialTrove.repository.MemberRepository;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Service
//public class MemberDetailsService implements UserDetailsService {
//
//    private final MemberRepository memberRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//        MemberEntity member = memberRepository.findByUserId(userId)
//                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId));
//
//        return org.springframework.security.core.userdetails.User
//                .withUsername(member.getUserId())
//                .password(member.getUserPw())
//                .authorities("USER")
//                .build();
//    }
//}
