package nana.TrialTrove.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ApplicationProductDTO {
    private Long id;
    private String image;
    private String productName;
    private String sellerName;
    private String location;
    private LocalDate applicationDate;

}
