package nana.TrialTrove.controller;

import nana.TrialTrove.domain.MemberDTO;
//import nana.TrialTrove.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/member/*")
@Controller
public class LoginController {


    @GetMapping("/login")
    public String loginView() {
        return "member/login";
    }

//    @PostMapping("/login")
//    public String login(@ModelAttribute MemberDTO memberDto, Model model) {
//        String userId = memberDto.getUserId();
//        String userPw = memberDto.getUserPw();
//
//        if (userId == null || userId.isEmpty() || userPw == null || userPw.isEmpty()) {
//            model.addAttribute("error", "아이디와 비밀번호를 모두 입력해주세요.");
//            return "member/login";
//        }
//
//        if (loginService.login(userId, userPw)) {
//            // 로그인 성공
//            return "redirect:/"; // 로그인 후 이동할 페이지 경로
//        } else {
//            // 로그인 실패
//            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
//            return "member/login";
//        }
//    }

    @GetMapping("/logout")
    public String logout() {
        // 로그아웃 처리 로직 추가
        return "redirect:/";
    }


}
