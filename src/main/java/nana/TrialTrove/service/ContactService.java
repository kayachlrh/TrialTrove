package nana.TrialTrove.service;


import nana.TrialTrove.domain.ContactDTO;
import nana.TrialTrove.domain.ContactEntity;
import nana.TrialTrove.repository.ContactRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



@Service
public class ContactService {


    private final ContactRepository contactRepository;
    private final ModelMapper modelMapper;



    //    @Autowired
//    private PasswordEncoder passwordEncoder;
    @Autowired
    public ContactService(ContactRepository contactRepository, ModelMapper modelMapper) {
        this.contactRepository = contactRepository;
        this.modelMapper = modelMapper;
    }

    // 게시글 생성
    @Transactional
    public ContactDTO createContact(ContactDTO contactDTO) {
        // 비밀번호를 BCrypt로 인코딩하여 저장
        //contactEntity.setPassword(passwordEncoder.encode(contactEntity.getPassword()));
        ContactEntity contactEntity = modelMapper.map(contactDTO, ContactEntity.class);
        ContactEntity savedContactEntity = contactRepository.save(contactEntity);
        return modelMapper.map(savedContactEntity, ContactDTO.class);
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public ContactDTO getContactByBno(Long bno) {
        ContactEntity contactEntity = contactRepository.findById(bno).orElse(null);
        return modelMapper.map(contactEntity, ContactDTO.class);
    }


    // 게시글 비밀번호 확인
    public ContactEntity getContactById(Long bno, String password) {
        // 게시글 ID로 게시글을 찾기
        Optional<ContactEntity> optionalContactEntity = contactRepository.findById(bno);

        // Optional로부터 ContactEntity를 가져온다
        if (optionalContactEntity.isPresent()) {
            ContactEntity contactEntity = optionalContactEntity.get();

            // 저장된 비밀번호와 입력된 비밀번호를 비교
            if (contactEntity.getPassword().equals(password)) {
                return contactEntity; // 비밀번호가 일치하면 ContactEntity를 반환
            }
        }

        return null; // 비밀번호가 일치하지 않거나 게시글을 찾을 수 없는 경우 null을 반환
    }


    // 게시글 조회
    @Transactional(readOnly = true)
    public Page<ContactDTO> getContactPage(Pageable pageable) {
        Page<ContactEntity> contactPage = contactRepository.findAll(pageable);
        return contactPage.map(contactEntity -> modelMapper.map(contactEntity, ContactDTO.class));
    }

    // 게시글 수정
    @Transactional
    public ContactDTO updateContact(ContactDTO updatedContact) {
        ContactEntity contactEntity = modelMapper.map(updatedContact, ContactEntity.class);
        contactEntity = contactRepository.save(contactEntity);
        return modelMapper.map(contactEntity, ContactDTO.class);
    }


    // 게시글 삭제
    public void deleteContact(Long bno) {
        contactRepository.deleteById(bno);

    }

    // 게시판 관리자 답변
    @Transactional
    public void saveAdminComment(Long bno, String adminComment) {
        // 게시글 조회
        ContactEntity contact = contactRepository.findByBno(bno)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. bno: " + bno));

        // 관리자 댓글 저장
        contact.setAdminComment(adminComment);

        contactRepository.save(contact);
    }

    // 게시판 관리자 답변 수정
    @Transactional
    public void updateAdminComment(Long bno, String newAdminComment) {
        // 게시글 조회
        ContactEntity contact = contactRepository.findByBno(bno)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. bno: " + bno));

        // 댓글 업데이트
        contact.setAdminComment(newAdminComment);

        contactRepository.save(contact);
    }

    // 게시판 관리자 답변 삭제
    public void deleteAdminComment(Long bno) {
        // bno로 게시글을 찾아서 adminComment 필드를 삭제
        Optional<ContactEntity> contactOptional = contactRepository.findByBno(bno);
        if (contactOptional.isPresent()) {
            ContactEntity contact = contactOptional.get();
            // adminComment 필드를 null로 업데이트
            contact.setAdminComment(null);
            contactRepository.save(contact);
        } else {
            // 해당 bno에 대한 게시글이 없는 경우 예외 처리
            throw new RuntimeException("해당 bno에 대한 게시글을 찾을 수 없습니다: " + bno);
        }
    }
}