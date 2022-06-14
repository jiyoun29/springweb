package ezenweb.Dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter  @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RoomDto {

    private String rname;
    private String x;
    private String y;
    private List<MultipartFile> rimg;
            //여러개 가져올 거니까 List 사용

    //MultipartFile : 첨부파일을 저장할 수 있는 인터페이스
    //cos vs MultipartFile의 인터페이스는 스프링에서 동일

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
