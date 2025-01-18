package nana.TrialTrove;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class MailControllerTest {

    @Autowired
    private JavaMailSender mailSender;

    @Test
    public void sendTestMail() {
        assertDoesNotThrow(() -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("test@example.com"); // 테스트용 이메일 주소
            message.setSubject("테스트 메일"); // 메일 제목
            message.setText("테스트용 메일 입니다."); // 메일 내용
            mailSender.send(message);
        });
    }
}
