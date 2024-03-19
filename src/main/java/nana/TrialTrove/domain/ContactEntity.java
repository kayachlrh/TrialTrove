package nana.TrialTrove.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Accessors(chain=true)
@Entity
@Table(name = "board")
@EntityListeners(AuditingEntityListener.class)
public class ContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String writer;

    private String content;


    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime regdate;

    private String password;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public ContactEntity() {
        this.regdate = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
    }

    private boolean deleted;

    private String adminComment;

    // Setter 메서드
    public void setAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }

}
