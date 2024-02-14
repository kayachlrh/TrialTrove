package nana.TrialTrove.controller;


import lombok.extern.slf4j.Slf4j;
import nana.TrialTrove.domain.MemberDTO;
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

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    // 회원가입 페이지
    @GetMapping("/join")
    public String joinView(Model model){
        model.addAttribute("message", "회원가입 페이지 진입");
        model.addAttribute("member", new MemberDTO());
        return "member/join";
    }
    // 회원가입
    @PostMapping("/join")
    public String joinMember(@ModelAttribute MemberDTO memberDTO) {
        memberService.joinMember(memberDTO);
        return "redirect:/member/login";
    }

    //아이디 중복체크
    @GetMapping("/checkDuplicate")
    @ResponseBody
    public boolean checkDuplicate(@RequestParam String userId) {
        return memberService.checkId(userId);
    }


    @GetMapping("/login")
    public String loginView() {
        log.info("로그인 페이지 진입");
        return "member/login";
    }

//    @PostMapping("/login")
//    public String login(String userId, String userPw, Model model) {
//        if (loginService.login(userId, userPw)) {
//            // 로그인 성공
//            return "redirect:/"; // 로그인 후 이동할 페이지 경로
//        } else {
//            // 로그인 실패
//            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
//            return "member/login";
//        }
//    }

}