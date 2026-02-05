//package nana.TrialTrove;
//
//import nana.TrialTrove.domain.MemberDTO;
//import nana.TrialTrove.domain.MemberEntity;
//import nana.TrialTrove.repository.MemberRepository;
//import nana.TrialTrove.service.MemberService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//public class MessageServiceTest {
//
//    @Autowired
//    private MemberService memberService;
//
//    @MockBean
//    private MemberRepository memberRepository;
//
//    @Test
//    void testGetAllUserExceptCurrent() {
//        // 테스트용 데이터 준비
//        MemberEntity member1 = new MemberEntity(1L, "user1", "password", "user1@example.com", "User One", null, null);
//        MemberEntity member2 = new MemberEntity(2L, "user2", "password", "user2@example.com", "User Two", null, null);
//
//        String currentUserId = "currentUser";
//
//        // MemberRepository Mock 설정: 현재 사용자(currentUser)를 제외한 회원 리스트 반환
//        when(memberRepository.findAllByUserIdNot(currentUserId)).thenReturn(List.of(member1, member2));
//
//        // 테스트 실행
//        List<MemberDTO> members = memberService.getAllUserExceptCurrent(currentUserId);
//
//        // 결과 검증
//        assertEquals(2, members.size(), "회원 목록의 크기가 예상과 다릅니다.");
//        assertTrue(members.stream().anyMatch(m -> m.getUserId().equals("user1")), "user1이 회원 목록에 없습니다.");
//        assertTrue(members.stream().anyMatch(m -> m.getUserId().equals("user2")), "user2가 회원 목록에 없습니다.");
//    }
////    @Test
////    public void testToEntity() {
////        // Given
////        MemberDTO memberDTO = new MemberDTO("user1");
////
////        // When
////        MemberEntity memberEntity = memberDTO.toEntity();
////
////        // Then
////        assertNotNull(memberEntity);
////        assertEquals("user1", memberEntity.getUserId());
////    }
//}