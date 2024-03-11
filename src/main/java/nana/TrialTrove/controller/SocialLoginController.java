package nana.TrialTrove.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/oauth/*")
@Controller
public class SocialLoginController {

    // 네이버 로그인
    @GetMapping("/naver")
    public String naverLogin() {
        return "redirect:/";
    }

}
