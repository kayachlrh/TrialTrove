package nana.TrialTrove.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import nana.TrialTrove.domain.ContactEntity;
import nana.TrialTrove.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RequestMapping("/board/*")
@Controller
@Slf4j
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // 게시글 작성 폼을 보여주는 페이지
    @GetMapping("/write")
    public String boardWriteForm(Model model) {
        model.addAttribute("contactEntity", new ContactEntity());
        return "board/write"; // createContent.html과 연결될 템플릿 파일명
    }

    // 게시글 작성 처리
    @PostMapping("/contact")
    public String createContact(@Valid @ModelAttribute("contactEntity") ContactEntity contactEntity, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // 유효성 검사 오류가 있을 경우
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                model.addAttribute(error.getField() + "Error", error.getDefaultMessage());
            }
            return "board/write"; // 오류가 있는 폼 페이지로 리다이렉트 또는 해당 오류 페이지로 이동
        } else {
            // 유효성 검사 통과 시 데이터를 저장하고 목록 페이지로 이동
            contactService.createContact(contactEntity);

            // 새로 추가된 데이터를 가져와서 목록에 추가
            model.addAttribute("contactEntity", contactEntity);
            model.addAttribute("activePage", "pages");

            return "redirect:/board/list"; // 게시판 메인 페이지로 리다이렉트
        }
    }

    // 게시판 내용 조회
    @GetMapping("/detail/{bno}")
    public String showContactDetail(@PathVariable(name = "bno") Long bno, Model model) {
        ContactEntity contactEntity = contactService.getContactByBno(bno);

        model.addAttribute("contactEntity", contactEntity);
        model.addAttribute("activePage", "pages");
        return "board/detail";

    }


    // 게시글 비밀번호 확인
    @PostMapping("/checkPassword/{bno}")
    public String checkPassword(@PathVariable(name = "bno") Long bno, @RequestParam("password") String password, Model model) {
        ContactEntity contact = contactService.getContactById(bno, password);

        if (contact != null) {
            // 비밀번호 일치
            model.addAttribute("contactEntity", contact);
            return "redirect:/board/detail/" + bno;
        } else {
            // 비밀번호 불일치
            return "board/passwordMismatch";
        }
    }

    // 게시글 목록 조회
    @GetMapping("/list")
    public String showContactList(Model model, @RequestParam(name ="page", defaultValue = "0") int page) {

        // 한 페이지에 표시할 게시글 수
        int pageSize = 10;

        // page에 해당하는 게시글 가져오기
        Page<ContactEntity> contactPage = contactService.getContactPage(PageRequest.of(page, pageSize));

        // 전체 페이지 수
        int totalPages = contactPage.getTotalPages();

        // 현재 페이지 번호
        int currentPage = contactPage.getNumber();

        // 페이징된 게시글 목록
        List<ContactEntity> contactList = contactPage.getContent();

        model.addAttribute("contactList", contactList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("activePage", "pages");

        return "board/list"; // contentList.html과 연결될 템플릿 파일명
    }

    // faq page
    @GetMapping("/faq")
    public String showFaqPage(Model model) {
        model.addAttribute("activePage", "pages");
        return "board/faq"; // FAQ 페이지를 나타내는 뷰의 이름을 반환
    }

    // 게시판 수정 페이지
    @Transactional
    @GetMapping("/modify/{bno}")
    public String showModifyForm(@PathVariable(name = "bno") Long bno, Model model) {
        ContactEntity contactEntity = contactService.getContactByBno(bno);

        model.addAttribute("contactEntity", contactEntity);

        // 수정 폼 페이지로 이동
        return "board/modify";
    }

    // 게시판 수정
    @PostMapping("/modify/{bno}")
    public String updateContact(@PathVariable(name = "bno") Long bno, @ModelAttribute ContactEntity updatedContact) {
        contactService.updateContact(updatedContact);
        return "redirect:/board/list"; // 수정 후 목록 페이지로 리다이렉트
    }



    // 게시판 삭제
    @PostMapping("/delete/{bno}")
    public String deleteContact(@PathVariable(name = "bno") Long bno) {
        contactService.deleteContact(bno);
        return "redirect:/board/list";
    }

}
