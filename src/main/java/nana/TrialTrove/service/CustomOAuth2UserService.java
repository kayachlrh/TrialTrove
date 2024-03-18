package nana.TrialTrove.service;

import lombok.RequiredArgsConstructor;
import nana.TrialTrove.domain.OAuthAttributes;
import nana.TrialTrove.domain.UserProfile;
import nana.TrialTrove.domain.UserRole;
import nana.TrialTrove.repository.UserProfileRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth 서비스(github, google, naver)에서 가져온 유저 정보를 담고있음
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // OAuth 서비스 이름(ex. github, naver, google)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth 로그인 시 키 값. 구글, 네이버, 카카오 등 각 다르기 때문에 변수로 받아서 넣음
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2 서비스의 유저 정보들
        Map<String, Object> attributes = oAuth2User.getAttributes();

        System.out.println("Received attributes from OAuth2 provider: " + attributes.toString());

        // registrationId에 따라 유저 정보를 통해 공통된 UserProfile 객체로 만들어 줌
        UserProfile userProfile = OAuthAttributes.extract(registrationId, attributes);

        UserProfile savedUserProfile = saveOrUpdateUserProfile(userProfile);

        return createDefaultOAuth2User(savedUserProfile, attributes, userNameAttributeName);
    }

    private UserProfile saveOrUpdateUserProfile(UserProfile userProfile) {
        // userProfileRepository에서 Email로 사용자를 찾아봅니다.
        Optional<UserProfile> userProfileOptional = userProfileRepository.findByEmail(userProfile.getEmail());

        // 찾은 사용자가 있으면 해당 사용자를 업데이트하고, 없으면 새로운 사용자로 저장합니다.
        if (userProfileOptional.isPresent()) {
            // 사용자 업데이트
            UserProfile existingUserProfile = userProfileOptional.get();
            // 업데이트할 사용자의 정보를 새로운 사용자 정보로 설정
            existingUserProfile.setName(userProfile.getName());
            // existingUserProfile.setEmail(userProfile.getEmail());
            // userProfileRepository를 사용하여 업데이트된 사용자를 저장
            userProfile = userProfileRepository.save(existingUserProfile);
        } else {
            // 새로운 사용자 저장
            userProfile = userProfileRepository.save(userProfile);
        }

        return userProfile;
    }

    private DefaultOAuth2User createDefaultOAuth2User(UserProfile userProfile, Map<String, Object> attributes,
                                                      String userNameAttributeName) {
        UserDetails userDetails = userProfile.toUserDetails();

        // User 객체의 역할 정보를 가져옴
        Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(UserRole.USER.getValue()));

        // DefaultOAuth2User 객체 생성하여 반환
        return new DefaultOAuth2User(
                authorities,
                attributes,
                userNameAttributeName
        );
    }
}

