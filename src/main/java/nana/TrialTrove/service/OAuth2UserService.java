package nana.TrialTrove.service;

import lombok.RequiredArgsConstructor;
import nana.TrialTrove.domain.SocialLoginDTO;
import nana.TrialTrove.domain.SocialLoginEntity;
import nana.TrialTrove.repository.SocialLoginRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final SocialLoginRepository socialLoginRepository;

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

        // registrationId에 따라 유저 정보를 통해 공통된 UserProfile 객체로 만들어 줌
        SocialLoginEntity socialLoginEntity = saveOrUpdateSocialLoginEntity(registrationId, attributes);

        return createOAuth2User(attributes, userNameAttributeName);
    }

    private SocialLoginEntity saveOrUpdateSocialLoginEntity(String registrationId, Map<String, Object> attributes) {
        String naverId = (String) attributes.get("naverId");
        String name = (String) attributes.get("name");
        String email = (String) attributes.get("email");

        // 이미 등록된 소셜 로그인 정보가 있는지 확인
        SocialLoginEntity socialLoginEntity = socialLoginRepository.findByNaverId(naverId);

        if (socialLoginEntity == null) {
            // 등록되지 않은 경우 새로운 SocialLoginEntity 생성
            socialLoginEntity = new SocialLoginEntity();
            socialLoginEntity.setNaverId(naverId);
            socialLoginEntity.setName(name);
            socialLoginEntity.setEmail(email);
            // SocialLoginEntity 저장
            socialLoginRepository.save(socialLoginEntity);
        }

        return socialLoginEntity;
    }

    private OAuth2User createOAuth2User(Map<String, Object> attributes,
                                        String userNameAttributeName) {
        return new DefaultOAuth2User(
                Collections.emptyList(), // 역할 정보를 빈 리스트로 설정
                attributes,
                userNameAttributeName
        );
    }
}
