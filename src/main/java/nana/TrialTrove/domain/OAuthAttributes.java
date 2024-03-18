package nana.TrialTrove.domain;


import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class OAuthAttributes {

    private String registrationId; // OAuth 서비스의 이름
    private String name; // 사용자 이름
    private String email; // 사용자 이메일

    public static UserProfile extract(String registrationId, Map<String, Object> attributes) {
        OAuthAttributes oauthAttributes = new OAuthAttributes();
        oauthAttributes.setRegistrationId(registrationId);

        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response != null) {
            oauthAttributes.setName((String) response.get("name"));
            oauthAttributes.setEmail((String) response.get("email"));
        }

        return oauthAttributes.toUserProfile(); // UserProfile 객체로 변환하여 반환
    }

    public UserProfile toUserProfile() {
        return new UserProfile(this.name, this.email);
    }
}