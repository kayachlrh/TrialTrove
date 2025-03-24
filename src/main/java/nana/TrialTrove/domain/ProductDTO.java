package nana.TrialTrove.domain;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.beans.ConstructorProperties;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    public static ProductDTO of(ProductEntity entity) {
        return ProductDTO.builder()
                .id(entity.getId())
                .productName(entity.getProductName())
                .image(entity.getImage())
                .sellerName(entity.getSellerName())
                .location(entity.getLocation())
                .deadlineDate(entity.getDeadlineDate())
                .applicants(entity.getApplicants())
                .maxApplicants(entity.getMaxApplicants())
                .description(entity.getDescription())
                .activityType(entity.getActivityType())
                .categoryName(entity.getCategory().getName()) // 카테고리 이름 가져오기
                .build();
    }

    // 체험 디테일 수정

//    public ProductDTO(Long id, String productName, String description, int maxApplicants) {
//        this.id = id;
//        this.productName = productName;
//        this.description = description;
//        this.maxApplicants = maxApplicants;
//    }
}
