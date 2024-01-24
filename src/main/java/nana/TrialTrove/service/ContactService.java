package nana.TrialTrove.service;

import jakarta.transaction.Transactional;
import nana.TrialTrove.domain.ContactEntity;
import nana.TrialTrove.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    // 모든 게시글 조회
    public List<ContactEntity> getAllContacts() {
        return contactRepository.findAll();
    }

    // 게시글 수정
    @Transactional
    public void modifyContact(Long bno, ContactEntity modifiedContact) {
        // 실제로 데이터를 수정하는 로직을 여기에 추가
        ContactEntity existingContact = contactRepository.findById(bno).orElse(null);

        if (existingContact != null) {
            existingContact.setTitle(modifiedContact.getTitle());
            existingContact.setWriter(modifiedContact.getWriter());
            existingContact.setContent(modifiedContact.getContent());
            existingContact.setPassword(modifiedContact.getPassword());

            contactRepository.save(existingContact);
        }
    }

}
