package nana.TrialTrove.controller;

import nana.TrialTrove.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/member/*")
@Controller
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String loginView() {
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
