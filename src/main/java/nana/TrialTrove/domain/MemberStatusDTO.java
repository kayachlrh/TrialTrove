package nana.TrialTrove.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberStatusDTO {
    private String userId;
    private int unreadMessageCount;
}
