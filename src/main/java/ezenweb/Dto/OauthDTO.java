package ezenweb.Dto;

import ezenweb.domain.member.MemberEntity;
import ezenweb.domain.member.Role;
import lombok.*;

import java.util.Map;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor @Builder
public class OauthDTO {

    private String mid; //아이디
    private String mname; //이름
    private String memail; //이메일
    private String registrationId; //클라이언트id
    private Map<String, Object> attributes; // //인증 결과
    private String nameAttributeKey; //회원정보 요청 키

    //1. 클라이언트 구분 메소드[네이버 or 카카오]
    public static OauthDTO of(String registrationId, String nameAttributeKey, Map<String ,Object> attributes){

        if(registrationId.equals("naver")){
            return ofnaver( registrationId, nameAttributeKey, attributes ); //네이버를 반환해서 값을 넘겨줌
        } else if (registrationId.equals("kakao")){
            return ofkakao( registrationId, nameAttributeKey, attributes);
        }
        return null;
    }   
    
    //2. 만약에 registrationid가 네이버이면
    public static OauthDTO ofnaver(String registrationId, String nameAttributeKey, Map<String, Object> attributes){
        System.out.println("네이버로 로그인 했습니다.");

        System.out.println(nameAttributeKey);
        //p.208
        Map<String, Object> response = ( Map<String, Object> ) attributes.get(nameAttributeKey); //형변환
        String mid = ((String) response.get("email")).split("@")[0]; //이메일에서 아이디만 추출

        return OauthDTO.builder()
                .mid(mid)
                .mname( (String) response.get("name"))
                .memail( (String) response.get("email"))
                .registrationId(registrationId)
                .attributes(attributes)
                .nameAttributeKey(nameAttributeKey)
                .build();
    }

    //2. 만약에 registrationid가 카카오이면
    public static OauthDTO ofkakao(String registrationId, String nameAttributeKey, Map<String, Object> attributes){
        System.out.println("카카오로 로그인 했습니다.");
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get(nameAttributeKey); //담기
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile"); //key값으로 저장되어 있으므로 한 번 빼서 사용

        String mid = ( (String) kakao_account.get("email")).split("@")[0];

        return OauthDTO.builder()
                .mid(mid)
                .mname((String) profile.get("nickname"))
                .memail((String) kakao_account.get("email"))
                .registrationId(registrationId)
                .attributes(attributes)
                .nameAttributeKey(nameAttributeKey)
                .build();
    }

    //oauth 정보 -> DB저장을 위한 entity화
    public MemberEntity toentity(){

        return MemberEntity.builder()
                .mid(this.mid)
                .mname(this.mname)
                .memail(this.memail)
                .oauth(this.registrationId)
                .role(Role.MEMBER)
                .build();
    }

    
}
