package ezenweb.domain.room;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @ToString
@AllArgsConstructor  @NoArgsConstructor
@Builder
@Table(name = "room") // 테이블 이름 정의
public class RoomEntity { //Entity : 객체

    //@Id : 기본값. pk값
    // @GeneratedValue(strategy = GenerationType.IDENTITY) :  auto키로 추가
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rno;    //방번호 (pk, ak)
    private String rtitle; //방 타이틀
    private String rlat;   //경도
    private String rlon;   //위도
    //dto와 맞춘다

    private String rtrans; //거래방식(전세/월세/매매)
    private String rprice; //가격
    private String rarea; //면적
    private String rmanagmentfee; //관리비
    private String rstructure; //구조
    private String rcompletiondate; //준공날짜
    private boolean rparking; //주차여부
    private boolean relevator; //엘리베이터 여부
    private String rindate; //입주가능일
    private String ractive; // 거래상태
    private int rfloor; //층
    private int rmaxfloor; //건물층수
    private String rkind; //건물종류
    private String raddress; //주소
    @Column(columnDefinition = "TEXT")
    private String rcontents; //상세설명

//    private String rimg; 이미지 따로

                                    //cascade : 제약 조건
    @Builder.Default
    @OneToMany(mappedBy = "roomEntity", cascade = CascadeType.ALL) //pk특성
    private List<RoomimgEntity> roomimgEntitylist = new ArrayList<>(); //메모리 할당


}