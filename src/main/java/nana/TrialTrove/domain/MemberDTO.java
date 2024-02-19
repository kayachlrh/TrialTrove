package nana.TrialTrove.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {

    private Long id;

    @NotEmpty(message = "아이디는 필수 입력 항목입니다.")
    private String userId;

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).{8,20}$",
            message = "비밀번호는 숫자, 영문자, 특수문자를 포함해야합니다."
    )
    private String userPw;

    @Email(message = "올바른 이메일 주소가 아닙니다.")
    private String email;

    @NotEmpty(message = "이름은 필수 입력 항목입니다.")
    @Size(max = 20, message = "10자 이하만 가능합니다.")
    private String name;
}
