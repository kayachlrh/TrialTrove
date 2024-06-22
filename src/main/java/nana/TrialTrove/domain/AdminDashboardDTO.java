package nana.TrialTrove.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class AdminDashboardDTO {
    private String productName;
    private LocalDateTime applicationDate;
    private LocalDate deadlineDate;
    private String status;
    private Long productId;
    private Long applicationId;
    private String userId;

    public AdminDashboardDTO(String productName, LocalDateTime applicationDate, LocalDate deadlineDate, String status, Long productId, Long applicationId, String userId) {
        this.productName = productName;
        this.applicationDate = applicationDate;
        this.deadlineDate = deadlineDate;
        this.status = status;
        this.productId = productId;
        this.applicationId = applicationId;
        this.userId = userId;
    }

}
