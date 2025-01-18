package nana.TrialTrove.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import nana.TrialTrove.domain.MemberDTO;
import nana.TrialTrove.domain.MemberEntity;
import nana.TrialTrove.domain.PasswordForm;
import nana.TrialTrove.domain.PasswordResetForm;
import nana.TrialTrove.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/member/*")
@Controller
@Slf4j
public class LoginController {
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @Autowired
    private MemberService memberService;

    @Autowired
    private JavaMailSender mailSender;

    private Map<String, String> verificationCodes = new HashMap<>(); // 인증번호 저장
    private static final long CODE_EXPIRATION_TIME = 300000; // 5분 (밀리초)

    @GetMapping("/login")
    public String loginView() {
        return "member/login";
    }


    @PostMapping("/logout")
    public String logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        this.logoutHandler.logout(request, response, authentication);
        return "redirect:/";
    }

    // 아이디 찾기 페이지
    @GetMapping("/findId")
    public String findIdView() {
        return "member/find-id";
    }


    // 아이디 찾기 인증번호 발송
    @PostMapping("/validateUser")
    @ResponseBody
    public Map<String, Object> validateUser(@RequestBody MemberDTO requestDTO) {
        String email = requestDTO.getEmail();
        String name = requestDTO.getName();

        // 회원 정보 확인 ( 이메일과 이름)
        Optional<MemberDTO> member = memberService.findByEmailAndName(email, name);

        Map<String, Object> response = new HashMap<>();
        if (member.isEmpty()) {
            response.put("success", false);
            response.put("message", "회원정보가 일치하지 않습니다.");
            return response;
        }

        // 인증번호 생성 및 전송
        String code = String.valueOf((int) (Math.random() * 9000) + 1000); // 4자리 난수
        String value = code + ":" + System.currentTimeMillis(); // "인증번호:생성시간"
        verificationCodes.put(email, value);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("TT 인증번호 안내");
            message.setText("인증번호: " + code);
            mailSender.send(message);

            response.put("success", true);
            response.put("message", "인증번호가 이메일로 전송되었습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "메일 전송 실패:" + e.getMessage());
        }

        return response;
    }

    // 인증번호 검증
    @PostMapping("/verifyCode")
    @ResponseBody
    public Map<String, Object> verifyCode(@RequestBody Map<String, String> request, HttpSession session) {
        String email = request.get("email");
        String code = request.get("code");


        Map<String, Object> response = new HashMap<>();
        String value = verificationCodes.get(email);


        if (value == null) {
            response.put("success", false);
            response.put("message", "인증번호가 일치하지 않습니다.");
            return response;
        }

        // "인증번호:생성시간" 형식에서 값 추출
        String[] parts = value.split(":");
        String savedCode = parts[0];
        long timestamp = Long.parseLong(parts[1]);


        // 인증번호 검증
        if (!savedCode.equals(code)) {
            response.put("success", false);
            response.put("message", "인증번호가 일치하지 않습니다.");
            return response;
        }

        // 유효시간 확인
        if (System.currentTimeMillis() - timestamp > CODE_EXPIRATION_TIME) {
            verificationCodes.remove(email); // 만료된 인증번호 삭제
            response.put("success", false);
            response.put("message", "인증번호가 만료되었습니다.");
            return response;
        }

        // 인증번호 삭제
        verificationCodes.remove(email);

        // 이메일로 아이디 가져오기
        Optional<MemberDTO> member = memberService.findByEmail(email);
        if (member.isEmpty()) {
            response.put("success", false);
            response.put("message", "해당 이메일로 가입된 사용자가 없습니다.");
            return response;
        }

        // 가져온 아이디
        String userId = member.get().getUserId();
        session.setAttribute("userId", userId);

        response.put("success", true);
        response.put("message", "인증번호 확인 완료");

        return response;
    }

    // 아이디 찾기 결과 페이지
    @GetMapping("/findIdResult")
    public String findIdResultView(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/member/findId"; // 세션 데이터가 없으면 아이디 찾기 페이지로 리다이렉트
        }
        model.addAttribute("userId", userId);
        session.removeAttribute("userId"); // 결과 페이지를 벗어나면 세션 데이터 삭제
        return "member/findIdResult";
    }

    // 비밀번호 찾기 페이지
    @GetMapping("/findPw")
    public String findPwView() {
        return "member/find-pw";
    }


    // 비밀번호 찾기 인증번호 발송
    @PostMapping("/validatePw")
    @ResponseBody
    public Map<String, Object> pwValidateUser(@RequestBody MemberDTO requestDTO) {
        String email = requestDTO.getEmail();
        String userId = requestDTO.getUserId();

        // 회원 정보 확인 ( 이메일과 이름)
        Optional<MemberDTO> member = memberService.findByEmailAndUserId(email, userId);

        Map<String, Object> response = new HashMap<>();
        if (member.isEmpty()) {
            response.put("success", false);
            response.put("message", "회원정보가 일치하지 않습니다.");
            return response;
        }

        // 인증번호 생성 및 전송
        String code = String.valueOf((int) (Math.random() * 9000) + 1000); // 4자리 난수
        String value = code + ":" + System.currentTimeMillis(); // "인증번호:생성시간"
        verificationCodes.put(email, value);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("TT 인증번호 안내");
            message.setText("인증번호: " + code);
            mailSender.send(message);

            response.put("success", true);
            response.put("message", "인증번호가 이메일로 전송되었습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "메일 전송 실패:" + e.getMessage());
        }

        return response;
    }

    // 비밀번호 찾기 인증번호 검증
    @PostMapping("/findPw")
    @ResponseBody
    public Map<String, Object> pwVerifyCode(@RequestBody Map<String, String> request, HttpSession session) {
        String userId = request.get("userId");
        String email = request.get("email");
        String code = request.get("code");

        Map<String, Object> response = new HashMap<>();
        String value = verificationCodes.get(email);

        if (value == null) {
            response.put("success", false);
            response.put("message", "인증번호가 일치하지 않습니다.");
            return response;
        }

        // 인증번호 및 유효시간 검증
        String[] parts = value.split(":");
        String savedCode = parts[0];
        long timestamp = Long.parseLong(parts[1]);
        if (!savedCode.equals(code) || System.currentTimeMillis() - timestamp > CODE_EXPIRATION_TIME) {
            verificationCodes.remove(email); // 만료된 인증번호 삭제
            response.put("success", false);
            response.put("message", "인증번호가 만료되었거나 일치하지 않습니다.");
            return response;
        }

        // 이메일과 아이디로 사용자 확인
        Optional<MemberDTO> member = memberService.findByEmailAndUserId(email, userId);
        if (member.isPresent()) {
            // 인증 성공: 세션에 사용자 ID 저장
            session.setAttribute("userId", userId);
            response.put("success", true);
            response.put("message", "인증번호 확인 완료. 비밀번호 재설정 페이지로 이동합니다.");
        } else {
            response.put("success", false);
            response.put("message", "이메일과 아이디가 일치하지 않습니다.");
        }

        return response;
    }

    // 비밀번호 재설정 페이지
    @GetMapping("/findPwReset")
    public String resetPasswordPage(HttpSession session, Model model) {
        // 세션에서 인증된 사용자 ID 확인
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/member/findPw"; // 인증 실패, 리다이렉트
        }

        model.addAttribute("passwordResetForm", new PasswordResetForm());
        model.addAttribute("userId", userId);
        return "member/findPwReset"; // 비밀번호 재설정 페이지
    }


    // 비밀번호 재설정
    @PostMapping("/findPwReset")
    public String resetPassword(@Valid @ModelAttribute PasswordResetForm passwordResetForm,
                                BindingResult bindingResult,
                                HttpSession session,
                                Model model) {

        // 유효성 검증 에러 처리
        if (bindingResult.hasErrors()) {
            log.debug("유효성 검사 실패: {}", bindingResult.getAllErrors());
            return "member/findPwReset"; // 유효성 검사 실패 시 다시 폼 페이지로 이동
        }

        // 비밀번호 확인
        if (!passwordResetForm.getNewPassword().equals(passwordResetForm.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "비밀번호가 일치하지 않습니다.");
            return "member/findPwReset";
        }

        // 세션에서 인증된 사용자 Id 확인
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/member/findPw";
        }

        // 비밀번호 변경 처리
        boolean isPasswordChanged = memberService.resetPassword(userId, passwordResetForm.getNewPassword());
        if (!isPasswordChanged) {
            bindingResult.rejectValue("newPassword", "error.newPassword", "이전 비밀번호와 동일하게 설정할 수 없습니다.");
            return "member/findPwReset";
        }

        // 성공 메시지 추가
        model.addAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
        return "member/login";
    }
}