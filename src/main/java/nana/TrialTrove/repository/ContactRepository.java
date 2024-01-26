package nana.TrialTrove.repository;

import nana.TrialTrove.domain.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
    // 게시글 상세 내용 조회
    ContactEntity findByBno(Long bno);


    //게시글 삭제
   // List<ContactEntity> findByDeletedFalse();
}
