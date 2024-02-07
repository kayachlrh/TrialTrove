package nana.TrialTrove.repository;

import nana.TrialTrove.domain.ContactEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
    // 게시글 상세 내용 조회
    ContactEntity findByBno(Long bno);

    // 삭제되지 않은 게시글 목록 조회
    Page<ContactEntity> findByDeletedFalse(Pageable pageable);


}
