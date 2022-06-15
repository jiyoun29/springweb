package ezenweb.domain.room;

import lombok.*;

import javax.persistence.*;

@Getter  @Setter  @ToString
@AllArgsConstructor @NoArgsConstructor @Builder
@Entity @Table(name = "rooming")
public class RoomimgEntity {

    //연결
        //pk번호
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rimgno;
        //이미지이름
    private String rimg;

        //방번호[fk]
   @ManyToOne //fk특성
   @JoinColumn(name = "rno") //rno와 join
   private RoomEntity roomEntity;

}