package nana.TrialTrove;

import nana.TrialTrove.domain.MemberDTO;
import nana.TrialTrove.domain.MemberEntity;
import nana.TrialTrove.repository.MemberRepository;
import nana.TrialTrove.service.MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;


    @Test
    public void testJoinView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/member/join"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("member/join"));
    }

    @Test
    public void testJoinMember() throws Exception {
        // 테스트에 사용할 MemberDTO 객체 생성
        MemberDTO mockMemberDTO = new MemberDTO();
        mockMemberDTO.setUserId("testUser");
        mockMemberDTO.setUserPw("testPassword");
        mockMemberDTO.setEmail("test@example.com");
        mockMemberDTO.setName("Test User");

        // memberService.joinMember 메서드가 호출되는지 확인
        mockMvc.perform(MockMvcRequestBuilders.post("/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"testUser\",\"userPw\":\"testPassword\",\"email\":\"test@example.com\",\"name\":\"Test User\"}"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/member/join")); // 여기를 /member/join으로 수정

        // memberService.joinMember 메서드가 한 번 호출되었는지 검증
        Mockito.verify(memberService, Mockito.times(1)).joinMember(any(MemberDTO.class));
    }

    @Test
    public void testLoginView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/member/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("member/login"));
    }

//    @Test
//    public void testLoginSuccess() throws Exception {
//        when(loginService.login("testUser", "testPassword")).thenReturn(true);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/member/login")
//                        .param("userId", "testUser")
//                        .param("userPw", "testPassword"))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
//    }
//
//    @Test
//    public void testLoginFailure() throws Exception {
//        when(loginService.login("invalidUser", "invalidPassword")).thenReturn(false);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/member/login")
//                        .param("userId", "invalidUser")
//                        .param("userPw", "invalidPassword"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
//                .andExpect(MockMvcResultMatchers.view().name("member/login"));
//    }

    @Test
    public void testFindByUserId() {
        // 가짜 사용자 ID와 가짜 사용자 엔터티 생성
        String userId = "testUser";
        MemberEntity fakeMember = new MemberEntity();
        fakeMember.setUserId(userId);

        // findByUserId 메서드가 호출될 때 가짜 사용자 엔터티 반환하도록 설정
        when(memberRepository.findByUserId(userId)).thenReturn(Optional.of(fakeMember));

        // 메서드 호출
        Optional<MemberEntity> optionalMember = memberRepository.findByUserId(userId);

        // 결과 확인
        assertTrue(optionalMember.isPresent());
        assertEquals(fakeMember.getUserId(), optionalMember.get().getUserId());
    }
}

