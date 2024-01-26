package nana.TrialTrove.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain=true)
@Entity
@Table(name = "board")
@EntityListeners(AuditingEntityListener.class)
public class ContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @NotEmpty(message = "제목은 필수 입력 항목입니다.")
    @Size(max = 20, message = "20자 이하만 가능합니다.")
    private String title;

    @NotEmpty(message = "내용은 필수 입력 항목입니다.")
    private String content;

    @NotEmpty(message = "회원 이름은 필수 입력 항목입니다.")
    @Size(max = 5, message = "회원 이름은 최대 5자까지 입력 가능합니다.")
    private String writer;

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime regdate;

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^[0-9]{4}$", message = "비밀번호는 숫자 4자리만 입력 가능합니다.")
    private String password;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    private boolean is_deleted;



}
