package nana.TrialTrove.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
@Setter
@Entity
public class UserProfile {

    @Id
    private String oauthId; // OAuth 고유 식별자
    private String userId;
    private String name;
    private String email;

    public User toUser() {
        return new User(this.userId, this.email, Collections.emptyList());
    }
}

