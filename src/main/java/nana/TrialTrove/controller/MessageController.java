package nana.TrialTrove.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nana.TrialTrove.domain.*;
import nana.TrialTrove.repository.MemberRepository;
import nana.TrialTrove.repository.MessageRepository;
import nana.TrialTrove.service.MemberService;
import nana.TrialTrove.service.MessageService;
import nana.TrialTrove.service.OnlineUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final MessageService messageService;
    private final MemberService memberService;
    private final OnlineUserService onlineUserService;


    // 메시지 페이지 뷰 반환
    @GetMapping("/message")
    public String messageView(Model model, Principal principal) {
        String currentUserId = principal.getName();
        List<MemberDTO> members = memberService.getAllUserExceptCurrent(currentUserId);

        log.info("Current User ID: {}", currentUserId);
        log.info("Members: {}", members);


        model.addAttribute("members", members);

        return "member/message";
    }


    // 메시지 조회 (특정 사용자와의 메시지 불러오기)
    @GetMapping("/messages")
    public ResponseEntity<List<MessageDTO>> getMessages(@RequestParam(name = "receiver") String receiverUserId,
                                                        Principal principal) {
        String senderUserId = principal.getName();

        // 두 사용자의 대화 내역을 가져옵니다
        List<MessageDTO> messageDTOs = messageService.getMessages(senderUserId, receiverUserId);

        System.out.println("보낸 사람 ID: " + senderUserId);
        System.out.println("받은 사람 ID: " + receiverUserId);

        return ResponseEntity.ok(messageDTOs);
    }


    /**
     * ⚡ 실시간 메시지 전송 (STOMP WebSocket 사용)
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/chat")
    public MessageDTO handleMessage(MessageDTO messageDTO) {

        System.out.println("Received Message: " + messageDTO.getContent()); // ✅ 서버에서 메시지 받았는지 확인

        // 클라이언트에서 보내온 String 타입의 senderUserId를 사용하여 MemberEntity를 찾기
        MemberEntity sender = memberRepository.findByUserId(messageDTO.getSenderUserId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        // receiver도 동일
        MemberEntity receiver = memberRepository.findByUserId(messageDTO.getReceiverUserId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // 2. 메시지 저장
        MessageEntity message = MessageEntity.builder()
                .sender(sender)         // sender는 MemberEntity로 연결됨 (id로 저장)
                .receiver(receiver)      // receiver도 MemberEntity로 연결됨 (id로 저장)
                .content(messageDTO.getContent())
                .timestamp(LocalDateTime.now())
                .build();
        messageRepository.save(message);


        return MessageDTO.of(message);
    }

    // 사용자 온라인 상태 업데이트
    @MessageMapping("/user.online")
    @SendTo("/topic/onlineUsers")
    public List<MemberStatusDTO> userConnected(StompHeaderAccessor headerAccessor) {
        if (headerAccessor.getUser() == null) {
            throw new RuntimeException("사용자가 로그인되어 있지 않습니다.");
        }

        String userId = headerAccessor.getUser().getName();
        onlineUserService.userConnected(userId);

        log.info("User Online: {}", userId);
        return onlineUserService.getOnlineUsers().stream()
                .map(user -> new MemberStatusDTO(user, messageService.getUnreadMessageCount(user)))
                .collect(Collectors.toList());
    }


    // 사용자 오프라인 처리
    @MessageMapping("/user.offline")
    @SendTo("/topic/onlineUsers")
    public Set<String> userDisconnected(StompHeaderAccessor headerAccessor) {
        if (headerAccessor.getUser() == null) {
            throw new RuntimeException("사용자가 로그인되어 있지 않습니다.");
        }

        String userId = headerAccessor.getUser().getName(); // 현재 로그인한 유저 아이디 가져오기
        onlineUserService.userDisconnected(userId);

        log.info("User Offline: {}", userId);
        return onlineUserService.getOnlineUsers(); // 현재 온라인 유저 목록 반환
    }

    // 🔹 미확인 메시지 개수 조회 API
    @GetMapping("/messages/unread/{receiverUserId}")
    public ResponseEntity<Map<String, Integer>> getUnreadMessages(@PathVariable(name = "receiverUserId") String receiverUserId) {
        Map<String, Integer> unreadCounts = messageService.getUnreadMessageCountForUser(receiverUserId);
        return ResponseEntity.ok(unreadCounts);
    }

    // 미확인 메시지 읽음 처리
    @PostMapping("/messages/mark-as-read/{senderUserId}")
    public ResponseEntity<?> markMessagesAsRead(@PathVariable(name = "senderUserId") String senderUserId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String receiverUserId = principal.getName(); // 현재 로그인한 유저(A)


        messageService.markMessagesAsRead(senderUserId, receiverUserId); // A가 받은 메시지만 읽음 처리
        return ResponseEntity.ok(Map.of("message", "읽음 처리 완료"));
    }
}
