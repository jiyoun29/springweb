package ezenweb.Dto;

import ezenweb.domain.room.RoomEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Getter  @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RoomDto {

    private int rno;    //방번호 (pk, ak)
    private String rtitle; //방 타이틀
    private String rlat;   //경도
    private String rlon;   //위도

//    private String rimg;
    private List<MultipartFile> rimg;
            //여러개 가져올 거니까 List 사용

    //MultipartFile : 첨부파일을 저장할 수 있는 인터페이스
    //cos vs MultipartFile의 인터페이스는 스프링에서 동일

    //dto->entity 메소드
        //1. 생성자
        //2. 빌드패턴 [빌더에 포함되지 않는 필드는 0 또는 null]
        //3. ModelMapper 라이브러리리

   private int rtrans; //거래방식(전세/월세/매매)
    private int rprice; //가격
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
    private String rcontents; //상세설명

    //dto -> 메소드를 바꿔주는 메소드 필요(반복적 사용을 위하여)
    //this로 호출
    public RoomEntity toentity() {
        return RoomEntity.builder()
            .rno(this.rno)
            .rtitle(this.rtitle)
            .rlat(this.rlat)
            .rlon(this.rlon)
            .rtrans(this.rtrans)
            .rprice(this.rprice)
            .rarea(this.rarea)
            .rmanagmentfee(this.rmanagmentfee)
            .rstructure(this.rstructure)
            .rparking(this.rparking)
            .relevator(this.relevator)
            .rindate(this.rindate)
            .ractive(this.ractive)
            .rfloor(this.rfloor)
            .rmaxfloor(this.rmaxfloor)
            .rkind(this.rkind)
            .raddress(this.raddress)
            .rcontents(this.rcontents)
            .build();


        //            .roomimgEntitylist(new ArrayList<>())

        //
    }


}
