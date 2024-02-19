package nana.TrialTrove.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "member")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "아이디는 필수 입력 항목입니다.")
    private String userId;


    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).{8,20}$",
            message = "비밀번호는 숫자, 영문자, 특수문자를 포함해야합니다." )
    private String userPw;


    @Email(message = "올바른 이메일 주소가 아닙니다.")
    private String email;

    @NotEmpty(message = "이름은 필수 입력 항목입니다.")
    @Size(max = 20, message = "10자 이하만 가능합니다.")
    private String name;

    //권한 반환
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority("user"));
//    }
//
//
//    @Override
//    public String getUsername() {
//        return userId;
//    }
//    @Override
//    public String getPassword() {
//        return userPw;
//    }
//
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true; // 계정 만료 여부, 여기서는 항상 true
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true; // 계정 잠김 여부, 여기서는 항상 true
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true; // 자격 증명 만료 여부, 여기서는 항상 true
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true; // 계정 활성 여부, 여기서는 항상 true
//    }
}


