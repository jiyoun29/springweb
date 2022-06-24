package ezenweb.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // 생성자
@Getter
public enum Role {
    /*자바 자료형 p.178
        1. 클래스
        2. 인터페이스
        3. 열거형(enum) [서로 연관된 필드들의 집합 구성]
    */

    //열거형
    MEMBER("ROLE_MEMBER", "회원"), //인덱스 0
    INTERME("ROLE_INTERME","중개인"), //등급 1개 더 생성/ 인덱스 1
    ADMIN("ROLE_ADMIN","관리자"); //인덱스 2

    //열거형에 들어가는 필드 항목들
    private final String key; //파이널 = 상수. [ 데이터가 고정. 값을 넣으면 빨간 줄 제거 ]
    private final String title; //혹은 @RequiredArgsConstructor 넣어주면 빨간줄 제거
//    private final  String mid;


}
