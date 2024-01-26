package nana.TrialTrove.service;

import jakarta.transaction.Transactional;
import nana.TrialTrove.domain.ContactEntity;
import nana.TrialTrove.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    // 게시글 생성
    public void createContact(ContactEntity contactEntity) {
        // 여기서 비밀번호 등의 유효성 검사 및 필요한 로직 수행 가능
        contactRepository.save(contactEntity);
    }

    // 게시글 상세 조회
    public ContactEntity getContactByBno(Long bno) {
        return contactRepository.findByBno(bno);
    }

    // 게시글 비밀번호 확인

    public ContactEntity getContactByBno(Long bno, String password) {
        ContactEntity contactEntity = contactRepository.findByBno(bno);

        if (contactEntity != null && contactEntity.getPassword().equals(password)) {
            return contactEntity;
        }

        return null;
    }


    // 모든 게시글 조회
    public List<ContactEntity> getAllContacts() {
        return contactRepository.findAll();
    }

    // 게시글 수정
    public ContactEntity updateContact(ContactEntity updatedContact) {
        return contactRepository.save(updatedContact);
    }

    //게시글 삭제


}
