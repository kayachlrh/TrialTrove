package nana.TrialTrove.repository;

import nana.TrialTrove.domain.MemberEntity;
import nana.TrialTrove.domain.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    // 특정 사용자가 주고받은 메시지를 조회
    List<MessageEntity> findBySenderIdAndReceiverId(Long sender, Long receiver);

    int countByReceiverIdAndIsReadFalse(Long receiverId);

    // B → A 메시지 중 읽지 않은 메시지를 가져옴
    List<MessageEntity> findBySenderIdAndReceiverIdAndIsReadFalse(Long senderId, Long receiverId);

    // 🔹 특정 사용자의 미확인 메시지 개수를 발신자별로 그룹화하여 가져옴
    @Query("SELECT m.sender.userId, COUNT(m) FROM MessageEntity m " +
            "WHERE m.receiver.id = :receiverId AND m.isRead = false " +
            "GROUP BY m.sender.userId")
    List<Object[]> countUnreadMessagesBySender(@Param("receiverId") Long receiverId);
}
