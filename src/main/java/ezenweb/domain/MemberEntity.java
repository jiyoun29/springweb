package ezenweb.domain;

import lombok.Builder;

import javax.persistence.*;

@Entity
@Table(name="member")
@Builder
public class MemberEntity {

    //dto에서 복사해옴
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mno;
    private String mid;
    private String mpw;
    private String mname;

}