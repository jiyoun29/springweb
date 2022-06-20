package ezenweb.Dto;

import ezenweb.domain.Board.BoardEntity;
import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BoardDto {

    private int bno;            //게시물번호
    private String btitle;      //제목
    private String bcontent;    //내용
    private int bview;          //조회수
    private int blike;          //좋아요

    //Dto->Entity
    public BoardEntity toentity() {
        return BoardEntity.builder()
            .bno(this.bno)
            .btitle(this.btitle)
            .bcontent(this.bcontent)
            .bview(this.bview)
            .blike(this.blike)
            .build();
    }

}