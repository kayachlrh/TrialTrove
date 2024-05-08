package nana.TrialTrove.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordForm {

    @NotEmpty(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;

    @NotEmpty(message = "새 비밀번호를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).{8,20}$",
            message = "비밀번호는 숫자, 영문자, 특수문자를 포함해야 합니다."
    )
    private String newPassword;
}
