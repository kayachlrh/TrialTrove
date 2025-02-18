package nana.TrialTrove.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nana.TrialTrove.domain.MemberDTO;
import nana.TrialTrove.domain.MemberEntity;
import nana.TrialTrove.domain.PasswordForm;
import nana.TrialTrove.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Transactional
    public MemberDTO joinMember(@Valid MemberDTO memberDTO) {
        log.info("입력된 비밀번호: {}", memberDTO.getUserPw());
        // 패스워드 유효성 검사
        if (!isValidPassword(memberDTO.getUserPw())) {
            throw new IllegalArgumentException("비밀번호는 숫자, 영문자, 특수문자를 포함해야합니다");
        }
        // 패스워드 암호화
        String encryptedPassword = passwordEncoder.encode(memberDTO.getUserPw());


        // DTO를 엔티티로 변환
        MemberEntity memberEntity = modelMapper.map(memberDTO, MemberEntity.class);
        memberEntity.setUserPw(encryptedPassword);

        // 회원 저장
        memberRepository.save(memberEntity);

        return modelMapper.map(memberEntity, MemberDTO.class);
    }

    // 아이디 중복체크
    public boolean checkId(String userId) {
        return memberRepository.findByUserId(userId).isPresent();
    }

    public MemberEntity findMemberByUserId(String userId) {
        return memberRepository.findByUserId(userId).orElse(null);
    }

    // 비밀번호 유효성 검사
    private boolean isValidPassword(String userPw) {
        // 비밀번호가 숫자, 영문자, 특수문자를 모두 포함하는지 확인하는 정규 표현식
        String regex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!]).{8,20}$";
        // 정규 표현식을 이용하여 비밀번호 검사
        return userPw.matches(regex);
    }

    // 회원정보 가져오기
    public MemberDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // 인증되지 않은 사용자
        }
        String username = authentication.getName();
        MemberEntity currentUserEntity = memberRepository.findByUserId(username).orElse(null);
        MemberDTO currentUserDTO = modelMapper.map(currentUserEntity, MemberDTO.class);
        // 이메일과 이름 추가
        currentUserDTO.setEmail(currentUserEntity.getEmail());
        currentUserDTO.setName(currentUserEntity.getName());
        return currentUserDTO;
    }

    // 회원정보 업데이트
    public void updateUser(MemberDTO updatedUser) {
        // 현재 사용자 정보 업데이트
        MemberEntity currentUserEntity = memberRepository.findById(updatedUser.getId()).orElse(null);
        if (currentUserEntity != null) {
            currentUserEntity.setName(updatedUser.getName());
            currentUserEntity.setEmail(updatedUser.getEmail());
            // 다른 필드도 필요에 따라 업데이트할 수 있음
            memberRepository.save(currentUserEntity);
        } else {
            throw new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.");
        }
    }

    // 비밀번호 변경
    @Transactional
    public void updatePassword(String currentUserId, String newPassword) {

        // 현재 사용자의 아이디로 데이터베이스에서 사용자 정보를 가져옵니다.
        Optional<MemberEntity> optionalMember = memberRepository.findByUserId(currentUserId);

        if (optionalMember.isPresent()) {
            MemberEntity member = optionalMember.get();
            log.debug("사용자 정보를 성공적으로 가져왔습니다. 사용자 ID: {}", member.getUserId());

            // 새 비밀번호로 업데이트
            String hashedNewPassword = passwordEncoder.encode(newPassword);
            member.setUserPw(hashedNewPassword);
            memberRepository.save(member);
        } else {
            log.debug("해당 사용자를 찾을 수 없습니다. 사용자 ID: {}", currentUserId);
            throw new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.");
        }
    }

    // 회원 탈퇴

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Transactional
    public void deleteAccount(String currentUserId, String password) {
        // 사용자 정보 가져오기
        Optional<MemberEntity> optionalMember = memberRepository.findByUserId(currentUserId);
        if (optionalMember.isPresent()) {
            MemberEntity member = optionalMember.get();
            String encodedPassword = member.getPassword(); // 사용자의 해싱된 비밀번호

            // 비밀번호 검증
            if (passwordEncoder.matches(password, encodedPassword)) {
                // 회원 탈퇴 처리
                memberRepository.delete(member);
                // 추가적인 로직 수행 가능
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
    }


    // 회원 아이디 찾기
    public Optional<MemberDTO> findByEmailAndName(String email, String name) {
        Optional<MemberEntity> memberEntity = memberRepository.findByEmailAndName(email, name);

        return memberEntity.map(entity -> {
            MemberDTO memberDTO = modelMapper.map(entity, MemberDTO.class);
            return memberDTO;
        });
    }

    // 이메일로 아이디 조회
    public Optional<MemberDTO> findByEmail(String email) {
        Optional<MemberEntity> memberEntity = memberRepository.findByEmail(email);
        return memberEntity.map(entity -> modelMapper.map(entity, MemberDTO.class));
    }

    // 비밀번호 찾기
    public Optional<MemberDTO> findByEmailAndUserId(String email, String userId) {
        return memberRepository.findByEmailAndUserId(email, userId)
                .map(entity -> modelMapper.map(entity, MemberDTO.class));
    }

    // 비밀번호 업데이트
    public boolean resetPassword(String userId, String newPassword) {
        // 사용자 조회
        Optional<MemberEntity> memberOptional = memberRepository.findByUserId(userId);
        if (memberOptional.isEmpty()) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        MemberEntity member = memberOptional.get();

        // 이전 비밀번호와 동일한지 확인
        if (passwordEncoder.matches(newPassword, member.getUserPw())) {
            return false; // 이전 비밀번호와 동일하면 변경 불가
        }

        // 비밀번호 암호화 후 업데이트
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.setUserPw(encodedPassword);

        // 변경된 엔티티 저장
        memberRepository.save(member);
        return true;
    }

    // 회원 목록 가져오기
    public List<MemberDTO> getAllUserExceptCurrent(String currentUserId) {
        List<MemberEntity> members = memberRepository.findAllByUserIdNot(currentUserId);

        return members.stream()
                .map(member -> new MemberDTO(member.getId(), member.getUserId()))
                .collect(Collectors.toList());
    }
}

