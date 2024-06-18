package nana.TrialTrove.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"favorites", "products"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
@Entity
public class MemberEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String userPw;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "member_favorite",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<ProductEntity> favorites = new ArrayList<>();

    //권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }


    @Override
    public String getUsername() {
        return userId;
    }
    @Override
    public String getPassword() {
        return userPw;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부, 여기서는 항상 true
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠김 여부, 여기서는 항상 true
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 여부, 여기서는 항상 true
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성 여부, 여기서는 항상 true
    }

    @OneToMany(mappedBy = "ownerId")
    private List<ProductEntity> products = new ArrayList<>();
}



