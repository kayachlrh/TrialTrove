package nana.TrialTrove.domain;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.beans.ConstructorProperties;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ProductDTO {

    private Long id;
    private String productName;
    private String image;       // 이미지 경로
    private String sellerName;
    private String location;
    private LocalDate deadlineDate;
    private int applicants;
    private int maxApplicants;
    private MemberEntity ownerId; // MemberEntity의 id를 참조
    private Long categoryId; // Category의 id를 참조
    private String description;
    private String activityType;
    private String categoryName; // Category의 name 참조

    private MultipartFile imageFile; // 업로드된 이미지 파일

    // 체험 목록
    @ConstructorProperties({"id", "productName", "image", "sellerName", "location", "deadlineDate", "maxApplicants", "activityType", "categoryName", "description"})
    public ProductDTO(Long id, String productName, String image, String sellerName, String location, LocalDate deadlineDate, int maxApplicants, String description, String activityType, String categoryName) {
        this.id = id;
        this.productName = productName;
        this.image = image;
        this.sellerName = sellerName;
        this.location = location;
        this.deadlineDate = deadlineDate;
        this.maxApplicants = maxApplicants;
        this.description = description;
        this.activityType = activityType;
        this.categoryName = categoryName;
    }
}
