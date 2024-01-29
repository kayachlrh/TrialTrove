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

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    // 게시글 생성
    public void createContact(ContactEntity contactEntity) {
        // 비밀번호를 BCrypt로 인코딩하여 저장
        //contactEntity.setPassword(passwordEncoder.encode(contactEntity.getPassword()));
        contactRepository.save(contactEntity);
    }

    // 게시글 상세 조회
    public ContactEntity getContactByBno(Long bno) {
        return contactRepository.findByBno(bno);
    }


    // 게시글 비밀번호 확인
    public ContactEntity getContactById(Long bno, String password) {
        ContactEntity contactEntity = contactRepository.findByBno(bno);

        if (contactEntity != null) {
            String storedPassword = contactEntity.getPassword();
            System.out.println("Stored Password: " + storedPassword);
            System.out.println("Input Password: " + password);

            if (storedPassword.equals(password)) {
                return contactEntity;
            }
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
