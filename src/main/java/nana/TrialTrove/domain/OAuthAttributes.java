package nana.TrialTrove.domain;


import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class OAuthAttributes {

    private String registrationId; // OAuth 서비스의 이름
    private String userId; // OAuth 고유 식별자
    private String name; // 사용자 이름
    private String email; // 사용자 이메일

    public static UserProfile extract(String registrationId, Map<String, Object> attributes) {
        OAuthAttributes oauthAttributes = new OAuthAttributes();
        oauthAttributes.setRegistrationId(registrationId);
        oauthAttributes.setUserId((String) attributes.get("userId")); // 유저 정보에서 고유 식별자 추출
        oauthAttributes.setName((String) attributes.get("name")); // 유저 정보에서 이름 추출
        oauthAttributes.setEmail((String) attributes.get("email")); // 유저 정보에서 이메일 추출

        return oauthAttributes.toUserProfile(); // UserProfile 객체로 변환하여 반환
    }

    public UserProfile toUserProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setOauthId(this.userId);
        userProfile.setUserId(this.userId);
        userProfile.setName(this.name);
        userProfile.setEmail(this.email);
        return userProfile;
    }
}