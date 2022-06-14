package ezenweb.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @ToString
@AllArgsConstructor  @NoArgsConstructor
@Builder
@Table(name = "room")
public class RoomEntity {

    //pk값 auto키로 추가
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rno;
    private String roomname;
    private String x;
    private String y;
    //dto와 맞춘다

    private String rimg;


    private String rsell; //거래방식(전세/월세/매매)
    private String rprice; //가격
    private String rarea; //면적
    private String rcost; //관리비
    private String rplace; //구조
    private String rcondate; //준공날짜
    private String rcar; //주차여부
    private String rmove; //입주가능일
    private String rfloors; //층/건물층수
    private String rbuilding; //건물종류
    private String raddress; //주소
    private String rcontent; //상세설명


}
