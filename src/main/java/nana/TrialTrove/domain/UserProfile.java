package nana.TrialTrove.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

@Getter
@Setter
@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    public UserProfile() {
        // 기본 생성자 내용
    }

    public UserProfile(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserDetails toUserDetails() {
        String username = (this.name != null) ? this.name : "unknown";
        return User.withUsername(username)
                .password("") // 패스워드 필드는 사용하지 않음
                .roles("USER") // 사용자의 역할 설정
                .build();
    }
}
