package nana.TrialTrove.service;

import lombok.RequiredArgsConstructor;
import nana.TrialTrove.domain.MemberEntity;
import nana.TrialTrove.domain.MessageDTO;
import nana.TrialTrove.domain.MessageEntity;
import nana.TrialTrove.repository.MemberRepository;
import nana.TrialTrove.repository.MessageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MessageService {
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;

    // 특정 사용자와의 채팅 내역 조회
    public List<MessageDTO> getMessages(Long sender, Long receiver) {
        // ① 내가 보낸 메시지 (sender = 나, receiver = 상대방)
        List<MessageEntity> sentMessages = messageRepository.findBySenderIdAndReceiverId(sender, receiver);

        // ② 상대가 보낸 메시지 (sender = 상대방, receiver = 나)
        List<MessageEntity> receivedMessages = messageRepository.findBySenderIdAndReceiverId(receiver, sender);

        // ③ 두 리스트 합치고, 시간순 정렬
        List<MessageEntity> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(receivedMessages);
        allMessages.sort(Comparator.comparing(MessageEntity::getTimestamp)); // 시간순 정렬

        // ④ DTO로 변환 후 반환
        return allMessages.stream()
                .map(MessageDTO::of)  // MessageEntity → MessageDTO 변환
                .collect(Collectors.toList());
    }


    // 메시지 저장
    public MessageDTO saveMessage(MessageDTO messageDTO) {
        MemberEntity sender = memberRepository.findByUserId(messageDTO.getSenderUserId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        MemberEntity receiver = memberRepository.findByUserId(messageDTO.getReceiverUserId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        MessageEntity message = MessageEntity.builder()
                .sender(sender)
                .receiver(receiver)
                .content(messageDTO.getContent())
                .timestamp(LocalDateTime.now())
                .build();

        messageRepository.save(message);
        return MessageDTO.of(message);  // Entity -> DTO 변환 후 반환
    }


    // 🔹 특정 사용자의 미확인 메시지 개수 조회
    public Map<String, Integer> getUnreadMessageCountForUser(String receiverUserId) {
        MemberEntity receiver = memberRepository.findByUserId(receiverUserId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + receiverUserId));

        Long receiverId = receiver.getId();

        // 🔹 각 발신자(senderUserId)별로 미확인 메시지 개수 조회
        List<Object[]> result = messageRepository.countUnreadMessagesBySender(receiverId);
        Map<String, Integer> unreadCounts = new HashMap<>();

        for (Object[] row : result) {
            String senderUserId = (String) row[0];          // 발신자 ID
            int count = ((Number) row[1]).intValue();       // 미확인 메시지 개수
            unreadCounts.put(senderUserId, count);          // Map에 발신자 ID와 미확인 메시지 개수 저장
        }

        return unreadCounts;
    }

    // 사용자의 전체 미확인 메시지 개수 조회
    public int getUnreadMessageCount(String receiverUserId) {
        // 1️⃣ 문자열 userId를 Long id로 변환
        MemberEntity receiver = memberRepository.findByUserId(receiverUserId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + receiverUserId));

        Long receiverId = receiver.getId(); // DB에 저장된 Long 타입 ID

        // 2️⃣ 미확인 메시지 개수 조회 (자기 자신에게 보낸 메시지는 제외)
        return messageRepository.countByReceiverIdAndIsReadFalse(receiverId);
    }


    // 미확인 메시지 읽음 처리

    public void markMessagesAsRead(String senderUserId, String receiverUserId) {
        // 회원 ID를 조회
        Optional<MemberEntity> sender = memberRepository.findByUserId(senderUserId);
        Optional<MemberEntity> receiver = memberRepository.findByUserId(receiverUserId);

        // 만약 회원이 존재하지 않으면 예외 처리
        if (sender.isPresent() && receiver.isPresent()) {
            Long senderId = sender.get().getId();  // 메시지를 보낸 유저 (B)
            Long receiverId = receiver.get().getId();  // 로그인한 유저 (A)

            // 읽지 않은 메시지를 찾음 (B -> A)
            List<MessageEntity> unreadMessages = messageRepository
                    .findBySenderIdAndReceiverIdAndIsReadFalse(senderId, receiverId);

            // 메시지 읽음 처리
            unreadMessages.forEach(message -> message.setRead(true)); // 읽음 처리

            // 변경된 메시지를 저장
            messageRepository.saveAll(unreadMessages);
        } else {
            // 값이 없을 경우 예외 처리
            throw new RuntimeException("User not found");
        }
    }
}
