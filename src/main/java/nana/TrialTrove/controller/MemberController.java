package nana.TrialTrove.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import nana.TrialTrove.domain.MemberDTO;
import nana.TrialTrove.domain.MemberEntity;
import nana.TrialTrove.domain.PasswordForm;
import nana.TrialTrove.repository.MemberRepository;
import nana.TrialTrove.service.MemberDetailsService;
import nana.TrialTrove.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequestMapping("/member/*")
@Controller
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MemberDetailsService memberDetailsService;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public MemberController(MemberService memberService, MemberDetailsService memberDetailsService, MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.memberDetailsService = memberDetailsService;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
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

    // 아이디 중복체크
    @GetMapping("/checkDuplicate")
    @ResponseBody
    public boolean checkDuplicate(@RequestParam("userId") String userId) {
        return memberService.checkId(userId);
    }


    // MyInfo
    @GetMapping("/myInfo")
    public String myPage(Model model) {
        MemberDTO currentUser = memberService.getCurrentUser();
        if (currentUser == null) {
            // 현재 로그인한 사용자가 없는 경우 처리
            return "redirect:/login"; // 예를 들어 로그인 페이지로 리다이렉트
        }
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("passwordForm", new PasswordForm());
        return "member/myInfo";
    }

    // MyInfo 업데이트
    @PostMapping("/updateMyInfo")
    public String updateProfile(@RequestParam("name") String name, @RequestParam("email") String email) {
        // 현재 사용자 정보 가져오기
        MemberDTO currentUser = memberService.getCurrentUser();
        if (currentUser == null) {
            // 현재 로그인한 사용자가 없는 경우 처리
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }

        // 사용자 정보 업데이트
        currentUser.setName(name);
        currentUser.setEmail(email);
        memberService.updateUser(currentUser);

        return "redirect:/member/myInfo"; // 프로필 페이지로 리다이렉트
    }

    // 비밀번호 변경
    @PostMapping("/updatePassword")
    public String updatePassword(@Valid PasswordForm passwordForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("유효성 검사 실패: {}", bindingResult.getAllErrors());
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                model.addAttribute(error.getField() + "Error", error.getDefaultMessage());
                log.debug("필드 '{}'의 오류: {}", error.getField(), error.getDefaultMessage());
            }
            // 유효성 검사 실패
            return "redirect:/member/myInfo";
        }

        try {
            // 현재 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserId = authentication.getName();

            // 비밀번호 업데이트 호출
            memberService.updatePassword(currentUserId, passwordForm.getNewPassword());
            redirectAttributes.addFlashAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
            return "redirect:/member/myInfo";
        } catch (IllegalArgumentException e) {
            // 서비스에서 발생한 예외 처리
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/member/myInfo";
        }
    }

    @GetMapping("/delete")
    public String showDeleteForm() {
        return "member/deleteAccount"; // 탈퇴 페이지를 보여줄 뷰 이름
    }

    @PostMapping("/member/deleteAccount")
    public String deleteAccount(@RequestParam("password") String password, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 현재 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserId = authentication.getName();

            // 사용자 비밀번호 가져오기
            Optional<MemberEntity> optionalMember = memberRepository.findByUserId(currentUserId);
            if (!optionalMember.isPresent()) {
                throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
            }
            MemberEntity member = optionalMember.get();
            String encodedPassword = member.getPassword(); // 사용자의 해싱된 비밀번호

            // 비밀번호 검증
            if (!passwordEncoder.matches(password, encodedPassword)) {
                redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
                return "redirect:/member/delete"; // 비밀번호가 일치하지 않으면 다시 탈퇴 페이지로 리다이렉트
            }

            // 회원 탈퇴 호출
            memberService.deleteAccount(currentUserId, password);

            // 회원 탈퇴 성공 메시지 전달
            redirectAttributes.addFlashAttribute("successMessage", "회원 탈퇴가 성공적으로 처리되었습니다.");
            // 로그아웃
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            return "redirect:/member/login"; // 로그아웃 페이지로 리다이렉트
        } catch (IllegalArgumentException e) {
            // 서비스에서 발생한 예외 처리
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/member/delete"; // 회원 탈퇴 실패 시 다시 탈퇴 페이지로 리다이렉트
        }
    }
}