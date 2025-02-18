package nana.TrialTrove.repository;

import nana.TrialTrove.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByUserId(String userId);

    Optional<MemberEntity> findByEmail(String email);

    Optional<MemberEntity> findByEmailAndName(String email, String name);

    Optional<MemberEntity> findByEmailAndUserId(String email, String userId);

    // 현재 로그인한 회원을 제외한 전체 회원 목록 가져오기
    List<MemberEntity> findAllByUserIdNot(String userId);

}
