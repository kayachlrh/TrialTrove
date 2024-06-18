package nana.TrialTrove.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import nana.TrialTrove.domain.ContactDTO;
import nana.TrialTrove.domain.ContactEntity;
import nana.TrialTrove.service.ContactService;
import nana.TrialTrove.service.MemberService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;


@RequestMapping("/board/*")
@Controller
public class ContactController {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    private final ContactService contactService;
    private final ModelMapper modelMapper;

    @Autowired
    public ContactController(ContactService contactService, ModelMapper modelMapper) {
        this.contactService = contactService;
        this.modelMapper = modelMapper;
    }

    // 게시글 작성 폼을 보여주는 페이지
    @GetMapping("/write")
    public String boardWriteForm(Model model) {
        model.addAttribute("contactDTO", new ContactDTO());
        return "board/write"; // createContent.html과 연결될 템플릿 파일명
    }

    // 게시글 작성 처리
    @PostMapping("/contact")
    public String createContact(@ModelAttribute("contactDTO") @Valid ContactDTO contactDTO, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                model.addAttribute(error.getField() + "Error", error.getDefaultMessage());
            }
            return "board/write";
        } else {
            ContactDTO createdContactDTO = contactService.createContact(contactDTO);
            return "redirect:/board/list";
        }
    }

    // 게시판 내용 조회
    @GetMapping("/detail/{bno}")
    public String showContactDetail(@PathVariable(name = "bno") Long bno, Model model) {
        ContactDTO contactDTO = contactService.getContactByBno(bno);

        model.addAttribute("contactDTO", contactDTO);
        model.addAttribute("activePage", "pages");
        return "board/detail";

    }


    // 게시글 비밀번호 확인
    @PostMapping("/checkPassword/{bno}")
    public String checkPassword(@PathVariable(name = "bno") Long bno, @RequestParam("password") String password, Model model) {
        ContactEntity contactEntity = contactService.getContactById(bno, password);

        if (contactEntity != null) {
            // 비밀번호 일치
            model.addAttribute("contactDTO", contactEntity);
            return "redirect:/board/detail/" + bno;
        } else {
            // 비밀번호 불일치
            return "board/passwordMismatch";
        }
    }

    // 게시글 목록 조회
    @GetMapping("/list")
    public String showContactList(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {

        // 한 페이지에 표시할 게시글 수
        int pageSize = 10;

        // page에 해당하는 게시글 가져오기
        Page<ContactDTO> contactPage = contactService.getContactPage(PageRequest.of(page, pageSize));

        // 전체 페이지 수
        int totalPages = contactPage.getTotalPages();

        // 현재 페이지 번호
        int currentPage = contactPage.getNumber();

        // 페이징된 게시글 목록
        List<ContactDTO> contactList = contactPage.getContent();

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
    @GetMapping("/update/{bno}")
    public String showModifyForm(@PathVariable(name = "bno") Long bno, Model model) {
        ContactDTO contactDTO = contactService.getContactByBno(bno);

        model.addAttribute("contactDTO", contactDTO);

        // 수정 폼 페이지로 이동
        return "board/modify";
    }

    // 게시판 수정
    @PostMapping("/modify/{bno}")
    public String updateContact(@PathVariable(name = "bno") Long bno, @ModelAttribute ContactDTO updatedContact) {
        updatedContact.setBno(bno);
        contactService.updateContact(updatedContact);
        return "redirect:/board/list"; // 수정 후 목록 페이지로 리다이렉트
    }


    // 게시판 삭제
    @PostMapping("/delete/{bno}")
    public String deleteContact(@PathVariable(name = "bno") Long bno) {
        contactService.deleteContact(bno);
        return "redirect:/board/list";
    }


    // 게시판 관리자 답변
    @PostMapping("/reply/{bno}/add")
    public String saveAdminComment(@PathVariable(name = "bno") Long bno,
                                   @RequestParam(name = "adminComment") String adminComment) {

        contactService.saveAdminComment(bno, adminComment);
        return "redirect:/board/detail/" + bno;
    }

    // 게시판 관리자 답변 수정
    @PostMapping("/reply/update")
    public String updateComment(@RequestParam("bno") Long bno, @RequestParam("newComment") String newComment) {

        contactService.updateAdminComment(bno, newComment);
        return "redirect:/board/detail/" + bno; // 수정 후 게시글 상세 페이지로 리다이렉트
    }

    // 게시판 관리자 답변 삭제
    @PostMapping("/reply/{bno}/delete")
    public ResponseEntity<String> deleteAdminComment(@PathVariable(name = "bno") Long bno) {
        try {
            // 댓글 삭제 로직 호출
            contactService.deleteAdminComment(bno);
            return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제에 실패했습니다.");
        }
    }

}

