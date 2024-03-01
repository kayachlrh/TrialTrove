package nana.TrialTrove.repository;

import nana.TrialTrove.domain.SocialLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialLoginRepository extends JpaRepository<SocialLoginEntity, String> {
    SocialLoginEntity findByNaverId(String naverId);
}
