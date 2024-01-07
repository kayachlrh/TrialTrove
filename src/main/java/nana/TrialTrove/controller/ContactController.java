package nana.TrialTrove.controller;

import jakarta.validation.Valid;
import nana.TrialTrove.domain.ContactEntity;
import nana.TrialTrove.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@RequestMapping("/board/*")
@Controller
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
            return "board/write"; // 오류가 있는 폼 페이지로 리다이렉트 또는 해당 오류 페이지로 이동
        } else {
            // 유효성 검사 통과 시 데이터를 저장하고 목록 페이지로 이동
            contactService.createContact(contactEntity);

            // 새로 추가된 데이터를 가져와서 목록에 추가
            List<ContactEntity> updatedContactList = contactService.getAllContacts();
            model.addAttribute("contactList", updatedContactList);
            model.addAttribute("activePage", "pages");

            return "board/list"; // 게시판 메인 페이지로 리다이렉트
        }
    }

    // 게시글 목록 조회
    @GetMapping("/list")
    public String showContactList(Model model) {
        List<ContactEntity> contactList = contactService.getAllContacts();
        model.addAttribute("contactList", contactList);
        model.addAttribute("activePage", "pages");
        return "board/list"; // contentList.html과 연결될 템플릿 파일명
    }

    // faq page
    @GetMapping("/faq")
    public String showFaqPage(Model model) {
        model.addAttribute("activePage", "pages");
        return "board/faq"; // FAQ 페이지를 나타내는 뷰의 이름을 반환
    }
}
