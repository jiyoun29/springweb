package ezenweb.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomEntity {

    //pk값 auto키로 추가
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rno;
    private String roomname;
    private String x;
    private String y;
    //dto와 맞춘다

}
