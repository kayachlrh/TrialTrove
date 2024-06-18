package nana.TrialTrove.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = {"ownerId", "likedByMembers"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String productName;

    private String image;

    @Column(name = "seller_name")
    private String sellerName;

    private String location;

    @Column(name = "deadline_date")
    private LocalDate deadlineDate;

    private int applicants;

    @Column(name = "max_applicants")
    private int maxApplicants;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "activity_type")
    private String activityType;

    @ManyToOne
    @JoinColumn(name = "owner_id") // 외래 키 설정
    private MemberEntity ownerId;

    @ManyToOne
    @JoinColumn(name = "category_id") // 외래 키 설정
    private CategoryEntity category;

    @ManyToMany(mappedBy = "favorites")
    private List<MemberEntity> likedByMembers = new ArrayList<>();

}
