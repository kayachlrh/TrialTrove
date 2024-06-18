package nana.TrialTrove.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteDTO {
    private Long productId;
    private String productName;
    private String categoryName;
    private String location;
    private String image;
}
