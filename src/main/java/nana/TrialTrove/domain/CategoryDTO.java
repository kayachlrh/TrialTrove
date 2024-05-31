package nana.TrialTrove.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    private String name;
    private Long productCount;

    public CategoryDTO(String name, Long productCount) {
        this.name = name;
        this.productCount = productCount;
    }
}
