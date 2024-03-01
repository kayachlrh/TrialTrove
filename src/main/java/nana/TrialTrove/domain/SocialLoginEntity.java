package nana.TrialTrove.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SocialLoginEntity {
    @Id
    private String naverId;

    private String name;

    private String email;

}
