package nana.TrialTrove.repository;

import nana.TrialTrove.domain.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
    // 여기에 특별한 쿼리 메서드가 필요하면 추가할 수 있습니다.
}
