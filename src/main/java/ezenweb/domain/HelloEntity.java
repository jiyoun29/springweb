package ezenweb.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity //jpa : db내 테이블과 매핑(연결) //@Table(name = "hello")
@Getter //lombok
@NoArgsConstructor //롬복
public class HelloEntity {

    @Id //jpa
    @GeneratedValue(strategy = GenerationType.IDENTITY) //jpa : autokey
    private Long id;

        //기본값 : varchar
        // length : 필드 길이, nullable = null 포함 여부
    @Column(length = 500 , nullable = false) //jpa : Colimn(필드 속성명 = 값 , 속성명 = 값)
    private String title;

        //columnDefinition = "TEXT" : 긴글 자료형
    @Column(columnDefinition = "TEXT" , nullable = false)
    private String content;

    private String author;

}
