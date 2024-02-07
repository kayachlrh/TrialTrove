package nana.TrialTrove.controller;


import lombok.extern.slf4j.Slf4j;
import nana.TrialTrove.domain.MemberEntity;
import nana.TrialTrove.service.LoginService;
import nana.TrialTrove.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/member/*")
@Controller
@Slf4j
public class MemberController {


    private final MemberService memberService;
    private final LoginService loginService;

    @Autowired
    public MemberController(MemberService memberService, LoginService loginService) {

        this.memberService = memberService;
        this.loginService = loginService;
    }



    // 회원가입 페이지
    @GetMapping("/join")
    public String joinView(Model model){
        model.addAttribute("message", "회원가입 페이지 진입");
        return "member/join";
    }
    // 회원가입
    @PostMapping("/join")
    @ResponseBody
    public MemberEntity joinMember(@RequestBody MemberEntity member) {
        return memberService.joinMember(member.getUserId(), member.getUserPw(), member.getEmail(), member.getName());
    }


    @GetMapping("/login")
    public String loginView() {
        log.info("로그인 페이지 진입");
        return "member/login";
    }

    @PostMapping("/login")
    public String login(String userId, String userPw, Model model) {
        if (loginService.login(userId, userPw)) {
            // 로그인 성공
            return "redirect:/"; // 로그인 후 이동할 페이지 경로
        } else {
            // 로그인 실패
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "member/login";
        }
    }

}