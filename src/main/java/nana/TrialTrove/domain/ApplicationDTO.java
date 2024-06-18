package nana.TrialTrove.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ApplicationDTO {
    private Long id;

    private Long memberId;

    private Long productId;

    @NotEmpty(message = "번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^\\d{3}\\d{3,4}\\d{4}$", message = "유효한 전화번호 형식이 아닙니다.")
    private String phone;

    private LocalDateTime applicationDate;

    private String status;

}
