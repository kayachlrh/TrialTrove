package nana.TrialTrove.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import nana.TrialTrove.domain.MemberDTO;
import nana.TrialTrove.domain.MemberEntity;
import nana.TrialTrove.service.LoginService;
import nana.TrialTrove.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String joinView(Model model) {
        model.addAttribute("message", "회원가입 페이지 진입");
        model.addAttribute("member", new MemberDTO());
        return "member/join";
    }

    // 회원가입
    @PostMapping("/join")
    public String joinMember(HttpServletRequest request, @Valid @ModelAttribute("member") MemberDTO memberDTO, BindingResult bindingResult, Model model) {
        String isDuplicateChecked = request.getParameter("isDuplicateChecked");
        if (!"true".equals(isDuplicateChecked)) {
            // 중복 확인을 하지 않았을 경우 회원가입을 거부하고 회원가입 페이지로 리다이렉트
            return "redirect:/member/join"; // 회원가입 페이지 URL로 변경
        }

        if (memberService.checkId(memberDTO.getUserId())) {
            // 이미 존재하는 아이디일 경우 회원가입을 거부하고 회원가입 페이지로 리다이렉트
            return "redirect:/member/join"; // 회원가입 페이지 URL로 변경
        }
        if (bindingResult.hasErrors()) {

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                model.addAttribute(error.getField() + "Error", error.getDefaultMessage());
            }
            return "member/join";
        } else {
            memberService.joinMember(memberDTO);
            return "redirect:/member/login";
        }
    }

    //아이디 중복체크
    @GetMapping("/checkDuplicate")
    @ResponseBody
    public boolean checkDuplicate(@RequestParam("userId") String userId) {
        return memberService.checkId(userId);
    }


    @GetMapping("/login")
    public String loginView() {
        log.info("로그인 페이지 진입");
        return "member/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("userId") String userId, @RequestParam("userPw") String userPw, Model model) {
        if (userId == null || userId.isEmpty() || userPw == null || userPw.isEmpty()) {
            model.addAttribute("error", "아이디와 비밀번호를 모두 입력해주세요.");
            return "member/login";
        }

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