package nana.TrialTrove.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ContactDTO {

    private Long bno;

    @NotEmpty(message = "제목은 필수 입력 항목입니다.")
    @Size(max = 20, message = "20자 이하만 가능합니다.")
    private String title;

    @NotEmpty(message = "내용은 필수 입력 항목입니다.")
    private String content;

    @NotEmpty(message = "회원 이름은 필수 입력 항목입니다.")
    @Size(max = 5, message = "회원 이름은 최대 5자까지 입력 가능합니다.")
    private String writer;

    private LocalDateTime regdate;

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^[0-9]{4}$", message = "비밀번호는 숫자 4자리만 입력 가능합니다.")
    private String password;

    private LocalDateTime modifiedDate;

    private boolean deleted;

    private String adminComment;

    private LocalDateTime replyDate;


}
