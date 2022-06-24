package ezenweb.Dto;

import ezenweb.domain.member.MemberEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class LoginDto implements UserDetails { //로그인 세션에 넣을 Dto 생성
            //UserDetails -> authorities 필수 필드 선언

    //들고다닐 것들
    private int mno; //회원 번호
    private String mid; //회원 아이디
    private String mpw; //회원 비밀번호
    private String mname; //회원 이름
    private final Set<GrantedAuthority> authorities; //부여된 인증들

    //풀생성자
    public LoginDto(MemberEntity memberEntity, Collection< ? extends GrantedAuthority > authorityList ) { //Set<GrantedAuthority> authorities
        this.mno = memberEntity.getMno();
        this.mid = memberEntity.getMid();
        this.mpw = memberEntity.getMpw();
        this.mname = memberEntity.getMname();
        this.authorities = Collections.unmodifiableSet(new LinkedHashSet<>(authorityList));
    }



    //인증 검색
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }


    //패스워드 반환
    @Override
    public String getPassword() {
        return this.mpw;
    }

    //아이디 반환
    @Override
    public String getUsername() {
        return this.mid;
    }

    //계정 만료 여부 확인 [ true : 만료되지 않음 ]
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 잠겨 있는지 확인 [true : 잠겨있지 않음]
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //계정 패스워드 만료 여부 확인 [ true : 만료 되지 않음]
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정 사용 가능한 여부 확인 [ true : 사용 가능 ]
    @Override
    public boolean isEnabled() {
        return true;
    }
}

