package ezenweb.domain.Board;

import ezenweb.domain.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Builder @Entity
@Table(name="category")
public class CategoryEntity extends BaseTime { //보드용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cno;
    private String cname;
    // board 연관관계

    @Builder.Default
    @OneToMany(mappedBy = "categoryEntity" , cascade = CascadeType.ALL)
    private List<BoardEntity> boardEntityList = new ArrayList<>();

}
