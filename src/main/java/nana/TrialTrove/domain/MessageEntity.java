package nana.TrialTrove.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 메시지 ID (Primary Key)

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private MemberEntity sender; // 보낸 사람

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id", nullable = false)
    private MemberEntity receiver; // 받는 사람

    @Column(nullable = false)
    private String content; // 메시지 내용

    @Column(nullable = false)
    private LocalDateTime timestamp; // 메시지 전송 시간

    @Column(nullable = false)
    private boolean isRead = false; // 메시지 읽음 상태 (읽지 않은 상태로 기본 설정)

    // 읽음 상태 업데이트
    public void markAsRead() {
        this.isRead = true;
    }
}
