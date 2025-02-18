package nana.TrialTrove.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private Long id;

    @NotEmpty(message = "아이디는 필수 입력 항목입니다.")
    private String userId;


    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).{8,20}$",
            message = "비밀번호는 숫자, 영문자, 특수문자를 포함해야 합니다."
    )
    private String userPw;

    @Email(message = "올바른 이메일 주소가 아닙니다.")
    private String email;

    @NotEmpty(message = "이름은 필수 입력 항목입니다.")
    @Size(max = 20, message = "10자 이하만 가능합니다.")
    private String name;

    public MemberDTO(Long id, String userId) {
        this.id = id;
        this.userId = userId;
    }

//    public MemberEntity toEntity() {
//        MemberEntity memberEntity = new MemberEntity();  // 기본 생성자 사용
//        memberEntity.setUserId(this.userId);  // MemberDTO의 userId를 MemberEntity로 설정
//        // 필요에 따라 다른 필드들도 설정할 수 있습니다.
//        return memberEntity;
//    }
}
