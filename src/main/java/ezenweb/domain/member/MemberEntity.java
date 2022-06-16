package ezenweb.domain.member;

import ezenweb.domain.BaseTime;
import ezenweb.domain.room.RoomEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="member")
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor //풀생성자,빈생서자
public class MemberEntity extends BaseTime {

    //dto에서 복사해옴
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mno;
    private String mid;
    private String mpw;
    private String mname;

    @OneToMany(mappedBy = "memberEntity", cascade=CascadeType.ALL) //엔티티와 매핑
    List<RoomEntity> roomEntityList;
}

//extends : 상속[수퍼 클래스로부터 메모리 할당]
