package nana.TrialTrove.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String main(Model model){
        model.addAttribute("activePage", "home");
        return "main/index";
    }
}
