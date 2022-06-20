package ezenweb.domain.Board;

import ezenweb.domain.BaseTime;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter  @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Builder @Entity //테이블과매칭
public class BoardEntity extends BaseTime {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto
    private int bno;            //게시물번호
    private String btitle;      //제목
    private String bcontent;    //내용
    private int bview;          //조회수
    private int blike;          //좋아요
    //작성자, 카테고리, 첨부파일, 댓글[joincolumn으로 연관관계]

}
