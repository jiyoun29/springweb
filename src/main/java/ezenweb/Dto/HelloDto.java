package ezenweb.Dto;

import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Builder //생성자 사용 규칙을 어길 수 있음 -> 생성자를 만드는 데 있어서 안전성을 보장함(오류가 나는 걸 감수) [인수 순서, 개수, null/0 등]
public class HelloDto {

    //필드
    private String name;
    private int amount;

    //생성자 / 빈 생성자 : @NoArgsConstructor 풀 생성자 : @AllArgsConstructor

    //getset메소드 @Getter @Setter

    //toString @ToString


}
