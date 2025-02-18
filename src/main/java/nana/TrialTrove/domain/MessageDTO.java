package nana.TrialTrove.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {
    private Long id;
    private String senderUserId;  // 보낸 사람의 UserId (문자열)
    private String receiverUserId;  // 받은 사람의 UserId (문자열)
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;  // 메시지 읽음 상태

    // Entity에서 DTO로 변환하는 메서드
    public static MessageDTO of(MessageEntity entity) {
        return MessageDTO.builder()
                .id(entity.getId())
                .senderUserId(entity.getSender().getUserId())  // UserId를 문자열로 가져옴
                .receiverUserId(entity.getReceiver().getUserId())  // UserId를 문자열로 가져옴
                .content(entity.getContent())
                .timestamp(entity.getTimestamp())
                .isRead(entity.isRead())
                .build();
    }
}
