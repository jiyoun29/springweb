package ezenweb.Dto;

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


}
