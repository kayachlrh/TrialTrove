//package nana.TrialTrove.controller;

//import jakarta.servlet.http.HttpServletRequest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//
//@Controller
//@RequestMapping("/oauth/*")
//public class SocialLoginController {
//
//    private static final Logger log = LoggerFactory.getLogger(SocialLoginController.class);
//
//    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
//    private String naverClientId;
//
//    @GetMapping("/naver_login")
//    public String redirectToNaver() {
//        return "redirect:https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=" + naverClientId + "&redirect_uri=http://localhost:8080/login/oauth2/code/naver";
//    }
//
//    @GetMapping("/naver")
//    public String naverCallback(HttpServletRequest request) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication instanceof OAuth2AuthenticationToken) {
//            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
//            OAuth2User oauthUser = oauthToken.getPrincipal();
//
//            // 이하 코드는 OAuth2User에서 속성을 추출하여 사용하는 부분입니다.
//            String name = oauthUser.getAttribute("name");
//            String email = oauthUser.getAttribute("email");
//            log.info("User '{}' with email '{}' logged in via Naver OAuth.", name, email);
//        } else {
//            log.error("OAuth2 authentication token not found.");
//        }
//        return "redirect:/";
//    }
//}
