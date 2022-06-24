package ezenweb.Dto;

import ezenweb.domain.member.MemberEntity;
import ezenweb.domain.member.Role;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MemberDto {

    //필드
    private int mno;
    private String mid;
    private String mpw;
    private String mname;

    //DTO -> ENTITY
    //dto는 안정성 이유로 사용
    public MemberEntity toentitiy(){

        //패스워드 암호화
            //BCrypt : 레인보우 테이블 공격 방지를 위해 솔트Salt를 통합한 적용형 함수
            //랜덤의 Salt 부여하에 여러번 해시를 적용 -> 암호 해독이 어렵다
            //보안할 데이터 + 랜덤데이터 를 넣어서 -> 암호화 (난수를 만들어 진수로 변환)
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return MemberEntity.builder() // 빌더패턴 : 포함하지 않는 필드는 0 또는 null 자동 대입
                .mid( this.mid)
                .mpw(passwordEncoder.encode(this.mpw))
                .mname(this.mname)
                .roomEntityList( new ArrayList<>() )
                .role(Role.MEMBER) //권한 부여
                .build();
    }

}
