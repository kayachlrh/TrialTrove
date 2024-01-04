package nana.TrialTrove.service;

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

    // 모든 게시글 조회
    public List<ContactEntity> getAllContacts() {
        return contactRepository.findAll();
    }
}
