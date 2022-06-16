package ezenweb.Dto;

import ezenweb.domain.member.MemberEntity;
import lombok.*;

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
    public MemberEntity toentity(){
        return MemberEntity.builder() //빌더패턴 : 포함하지 않는 필드는 0 또는 null 자동 대입
            .mid(this.mid).mpw(this.mpw).mname(this.mname)
            .roomEntityList(toentity().getRoomEntityList())
            .build();


    }

}
