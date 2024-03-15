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


    @GetMapping("/logout")
    public String logout() {
        // 로그아웃 처리 로직 추가
        return "redirect:/";
    }


}
