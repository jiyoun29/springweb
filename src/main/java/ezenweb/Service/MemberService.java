package ezenweb.Service;

import ezenweb.Dto.LoginDto;
import ezenweb.Dto.MemberDto;
import ezenweb.Dto.OauthDTO;
import ezenweb.domain.member.MemberEntity;
import ezenweb.domain.member.MemberRepository;
import ezenweb.domain.message.MessageEntity;
import ezenweb.domain.message.MessageRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;    // 자바 메일 전송 인터페이스

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;


@Service
public class MemberService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {
                        /*UserDetailsService 인터페이스 [ 추상 메소드 존재 ]
                            -> loadUserByUsername
                        OAuth2UserService<OAuth2UserRequest, OAuth2User> : Oauth2 회원
                            -> loadUser 구현 */

    // 로그인(인증)된 회원의 아이디 찾기 메소드
    public String getloginmid(){
        // 1. 인증 객체 호출
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        // 2. 인정 정보 객체 호출
        Object principal = authentication.getPrincipal();

        if( principal.equals("anonymousUser") ){ // 로그인X
            return null;
        }else{ // 로그인O
            LoginDto loginDto = (LoginDto) principal;
            return loginDto.getMid();
        }
    }


    /* oauth2 서비스 제공 메소드
    OAuth2UserRequest : 인증 결과 호출 클래스
    */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        //인증[로그인 성공(인증)된 결과 정보 요청]
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest); //결과값을 가지고 있는 코드

//        System.out.println(oAuth2User.toString());

        //클라이언트 아이디값 [ 네이버 vs 카카오 vs 구글 ] oauth ; 구분용으로 사용할 변수
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //회원정보 요청시 사용되는 json 키 값 호출
        String userNameAttributeName = userRequest
                        .getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUserNameAttributeName();

        System.out.println("클라이언트(개발자)가 등록한 이름 : "+registrationId);
        System.out.println("회원정보(json) 호출 시 사용되는 키 이름 : "+ userNameAttributeName);
        System.out.println("회원정보(로그인) 결과 내용 : "+oAuth2User.getAttributes());


        //oauth2 정보 -> Dto -> entity -> db저장
        OauthDTO oauthDTO = OauthDTO.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        //1. 이메일로 엔티티 호출
        Optional<MemberEntity> optional = memberRepository.findBymemail(oauthDTO.getMemail());
        //2. 만약에 엔티티가 없으면
        if(!optional.isPresent()){
            memberRepository.save(oauthDTO.toentity()); //dto->entity 저장
        }


        //반환타입 DefaultOAuth2User(권한(role) ,회원인증정보, 회원정보 호출키)
            //DefaultOAuth2User, UserDetail : 반환시 인증 세션 자동부여[SimpleGrantedAuthority(필수)]
       return new DefaultOAuth2User(
               Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER")), //빨간느낌표 클릳ㄱ 후 자동생성
               oAuth2User.getAttributes(),
               userNameAttributeName
       );
    }

    /*
        1. 로그인 서비스 제공 메소드
        2. 패스워드 검증x [ 시큐리티 제공 ]
        3. 아이디만 검증 처리
        **/
    @Override
    public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException {
        
        System.out.println(mid); //넘어오는지 테스트

        //1. 회원 아이디로 엔티티 찾기
        Optional<MemberEntity> entityOptional = memberRepository.findBymid( mid );
        MemberEntity memberEntity = entityOptional.orElse(null);
                    /*Optional 클래스 [null 관련 오류 방지]
                        1. optional.isPresent() : null 아니면
                        2. .orElse : 만약에 optional객체가 비어있으면 반환할 데이터
                        만약 엔티티가 없으면 null 반환
                     */
        //2. 찾은 회원 엔티티의 권한[키]를 리스트에 담기
        List<GrantedAuthority> authorityList = new ArrayList<>();
                //GrantedAuthority : 부여된 인증의 클래스
                //List<GrantedAuthority> : 부여된 인증들을 모아두기

        System.out.println("권한 키:"+memberEntity.getrolekey());



        authorityList.add(new SimpleGrantedAuthority(memberEntity.getrolekey()));
            //리스트에 인증된 엔티티의 키를 보관

        return new LoginDto(memberEntity, authorityList); //회원 엔티티, 인증된 리스트 세션 부여
    }


    //서비스 구역 : 로직 / 트랙잭션

    //메모리 할당
    @Autowired //자동 빈(메모리) 생성 // @Autowired vs new
    private MemberRepository memberRepository;
    //@Autowired = new MemberRipository 개념

    @Autowired
    HttpServletRequest request; //세션 사용을 위한 request 객체 선언

    //1. 로그인처리 메소드 [ 시큐리티 사용 전 로그인 ]
//    public boolean login(String mid, String mpw){
//
//        //1. sql 없이 java로 처리, 모든 엔티티 호출[java처리 조건처리]
//        List<MemberEntity> memberEntityList = memberRepository.findAll();
//
//        //2. 모든 엔티티 리스트에서 입력받은 데이터와 비교한다.
//        for(MemberEntity entity : memberEntityList){
//            //3. 아이디와 비밀번호가 동일하면
//            if(entity.getMid().equals(mid) && entity.getMpw().equals(mpw)){
//
//                //로그인 세션에 사용될 dto 생성
//                LoginDto logindto = LoginDto.builder()
//                    .mno(entity.getMno())
//                    .mid(entity.getMid())
//                    .mname(entity.getMname())
//                    .build();
//
//                //세션 객체 호출                      //entity 대신 dto 삽입
//                request.getSession().setAttribute("login", logindto ); //mid는 변수니까 "" 사용 x
//                                                //세션 이름, 데이터
//                return true; //4.로그인 성공
//            }
//        }
//        return false; //4.로그인 실패
//    }
    
    
    //3. 로그아웃 메소드
    public void logout(){
        request.getSession().setAttribute("login",null); //해당 세션을 다시 null로 대입

        
    }


    //다른 클래스의 메소드나 필드 호출 방법(* 메모리 할당(객체 만들기))
        //1. static : java 실행시 우선 할당 -> java 종료시 메모리 초기화
        //2. 객체 생성 (p.106 참고)
            //1. 클래스명 객체명 = new 클래스명()
            //2. 객체명.set필드명 = 데이터
            //3. @Autowired
            //  클래스명 객체명;
            //이런 모양이 된다.


    @Autowired
    private JavaMailSender javaMailSender; //java 메일 전송 인터페이스

    //3. 메일전송 메소드
    public void mailsend(String toemail, String title, StringBuilder content){
                    //인수 : 받는사람이메일,제목,내용
        //SMTP : 간이 메일 전송 프로토콜 [텍스트 외 불가능]
        try { //이메일 전송
            MimeMessage message = javaMailSender.createMimeMessage(); //빨간줄 체크 삭제
                //Mime 프로토콜 : 텍스트 외 내용을 담는 프로토클 / SMTP와 같이 많이 사용됨
            //0.mime 설정
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8"); //예외처리
            //1. 보내는 사람
            mimeMessageHelper.setFrom("0u0_29@naver.com","Ezen 부동산");
            //2. 받는 사람
            mimeMessageHelper.setTo( toemail );
            //3. 메일 제목
            mimeMessageHelper.setSubject(title);
            //4. 메일 내용
            mimeMessageHelper.setText(content.toString(),true);
            //5. 메일 전송
            javaMailSender.send(message);
        } catch (Exception e) {System.out.println("메일전송실패"+e);}

    }


    //2. 회원가입처리 메소드
    @Transactional //세팃값 업데이트를 위해서 필수
    public boolean signup(MemberDto memberDto){
        //dto -> entity [ 이유 : Dto는 DB로 들어갈 수 없다 ]
        MemberEntity memberEntity = memberDto.toentitiy();
        System.out.println("가져오기"+memberDto.getMemail());
            //dto에 builder 만들었으므로 dto에서 호출한다.

        //entity 저장
        memberRepository.save(memberEntity);

        //저장여부 판단
        if(memberEntity.getMno() < 1){ //getmno를 위해 entity에 getter을 추가한다.
            return false; //회원가입 실패
        } else { //이메일에 들어가는 내용 [ html ]
            StringBuilder html = new StringBuilder(); //StringBuilder : 문자열 연결 클래스 [ append 연결메소드 vs +:연결연산자 ]
            html.append("<html><body><h1> EZEN 부동산 회원 이메일 검증 </h1>");
                //인증코드[문자 난수]만들기
            Random random = new Random(); //랜덤 객체
                StringBuilder authkey = new StringBuilder();
            for(int i = 0; i < 12 ; i++){ //12자리 문자열 난수 생성
                char randomchar = (char)(random.nextInt(26)+97); //97~122 //소문자 a->z난수 중 1개 발생
                authkey.append(randomchar); //생성된 문자 난수들을 하나씩 연결 -> 문자열 만들기
            } //인증코드 전달
            System.out.println("인증코드"+authkey);
            html.append("<a href='http://localhost:8081/member/email/"+authkey+"/"+memberDto.getMid()+"'>이메일 검증</a>");
            html.append("</body></html>");

            //해당 엔티티의 인증키 저장
                memberEntity.setOauth(authkey.toString());

            //회원가입 인증 메일 보내기
            mailsend(memberDto.getMemail(), "EZEN 부동산 회원가입 메일 인증", html );

            return true; //회원가입 성공
        }
    }

    @Transactional
    public boolean authsuccess(String authkey, String mid){
//        System.out.println("검증번호 : "+authkey+"회원아이디"+mid);
        //db 업데이트
        Optional<MemberEntity> optional = memberRepository.findBymid(mid);

        if(optional.isPresent()){ //optional이 null 아니면
            MemberEntity memberEntity = optional.get(); //해당 엔티티 가져오기
            if (authkey.equals(memberEntity.getOauth())) { //만약에 인증 키와 db내 인증키와 동일하면
                memberEntity.setOauth("Local"); //로칼로 보낸다.
                return true;
            }
        }
        return false;
    }




    //4, 회원수정 메소드
    @Transactional // find 외에는 전부 넣기
    public boolean update( String mname){

        //세션내 dto 호출 (형변환 필요)
        LoginDto loginDto = (LoginDto) request.getSession().getAttribute("login");
        if(loginDto == null){ //로그인이 안 되어 있는 상태. 세션이 없으면
            return false;
        }
                                                            //mno 들고 다니기
        MemberEntity memberEntity = memberRepository.findById(loginDto.getMno()).get();
        //findById : 반환타입 option 이므로 1회 get 해야한다
        memberEntity.setMname(mname); //해당 엔티티의 필드를 수정하면 자동으로 db도 수정 된다.
        return true;
    }



    //5. 회원 탈퇴 메소드
    public boolean delete(String mpw){

        //1. 세션 호출
        LoginDto loginDto = (LoginDto) request.getSession().getAttribute("login");
        //2. 엔티티 호출
        MemberEntity memberEntity = memberRepository.findById(loginDto.getMno()).get();
        //3. 삭제 처리
        if(memberEntity.getMpw().equals(mpw)){ //만약에 해당 로그인된 패스워드와 입력받은 패스워드가 같으면
            memberRepository.delete(memberEntity); //엔티티 삭제
            return true;
        }
        return false;
    }



    //6. 아이디 찾기 [이름과 이메일이 동일한 경우 프론트엔드에 표시]
    public String idfind(String mname, String memail){
        String idfind = null;
        //로직
        Optional<MemberEntity> optional = memberRepository.findId(mname, memail);

        if(optional.isPresent()){
            idfind = optional.get().getMid();
        }
        return idfind;
    }
    //구조??



    //7. 패스워드 찾기 [아이디 이메일이 동일한 경우 이메일로 임시(난수) 전송]
    @Transactional
    public boolean pwfind(String mid, String memail){
        Optional<MemberEntity> optional = memberRepository.findPw(mid, memail);
        if(optional.isPresent()){ //해당 엔티티를 찾았으면
            //1. 임시 비밀번호의 난수를 냉성한다.
            String tempw = "";
            for(int i = 0; i<12; i++) {//12자리
                Random random = new Random();
                char rchar = (char) (random.nextInt(58) + 65);
                tempw += rchar;
//                tempw = tempw + rchar;
            }
            System.out.println("임시 비밀번호 :"+tempw);
            //2. 현재 비밀번호를 임시비밀번호로 변경한다.
                //비크립트 방식의 암호화
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            optional.get().setMpw(passwordEncoder.encode(tempw)); //임시 비밀번호도 암호화가 필요하다.
            //3. 변경된 비밀번호를 이메일로 전송한다.
                //메일 내용 구현
            StringBuilder html = new StringBuilder();
                html.append("<html><body>"); //html 시작
                html.append("<div>회원님의 임시 비밀번호가 발급 되었습니다.</div>");
                html.append("<div>"+tempw+"</div>");
                html.append("</body></html>"); //html 끝
            //메일 전송 메소드 호출
            mailsend(optional.get().getMemail(), "EZEN 부동산 회원 임시 비밀번호", html);
            return true;
        }
        //해당 엔티티를 못 찾았으면
        return false;
    }


    //이메일 인증 여부 확인
    public int authmailcheck(String mid){
        Optional<MemberEntity> optional = memberRepository.findBymid(mid);
        if(optional.isPresent()){   //해당 엔티티 찾아
            if(optional.get().getOauth().equals("Local")){ //이메일 인증이 된 회원이면
                return 1; //일반회원
            } else if(optional.get().getOauth().equals("kakao")){
                return 2; //카카오 회원이면
            } else if(optional.get().getOauth().equals("naver")){
                return 3; //네이버 회원이면
            }
        }
        return 0; //실패
    }




    //----------- 메시지 ---------------------------------------------------
    // 1. 메시지 전송 메소드
    @Autowired
    private MessageRepository messageRepository;
    @Transactional
    public boolean messagesend(JSONObject object){
        // 1. JSON 정보 호출
        String from = (String) object.get("from");
        String to = (String) object.get("to");
        String msg = (String) object.get("msg");
        // 2. 각 회원들의 엔티티 찾기
        // 1. 보낸사람의 엔티티
        MemberEntity fromentity = null;
        Optional<MemberEntity> optionalMemberEntity1 = memberRepository.findBymid( from );
        if( optionalMemberEntity1.isPresent() ){ fromentity = optionalMemberEntity1.get(); }
        else{ return false; }
        // 2. 받는사람의 엔티티
        MemberEntity toentity = null;
        Optional<MemberEntity> optionalMemberEntity2 = memberRepository.findBymid(to);
        if( optionalMemberEntity2.isPresent() ){ toentity = optionalMemberEntity2.get(); }
        else{ return false; }
        // 3. 메시지 엔티티 생성
        MessageEntity messageEntity
                = MessageEntity.builder().msg(msg).fromentity(fromentity).toentity(toentity).build();
        // 4. 메시지 세이브
        messageRepository.save( messageEntity );
        // 각 회원에 메시지 fk 주입 [ 수정 ]
        fromentity.getFromentitylist().add( messageEntity ); // 보낸사람 엔티티의 보낸메시지 리스트에 메시지 저장
        toentity.getToentitylist().add( messageEntity );        //  받는사람 엔티티의 받은메시지 리스트에 메시지 저장
        return  true;
    }


    // 2. 안읽은 메시지 개수 메소드
    public Integer getisread(){
        //1. 로그인(인증)된 회원의 아이디
        String mid = getloginmid(); //위에 만들어놓은 것
        if(mid == null ){return -1;} //카운트가 최소0인데 -1이라는 것은 로그인이 안 됐다는 뜻

        int count = 0;

//        for(MessageEntity message : memberRepository.findBymid(mid).get().getToentitylist()){
//            if(! message.isIsread()) {count ++;} //만약에 isread() false이면 안 읽은 메세지 개수 증가
//        }

        int mno = memberRepository.findBymid(mid).get().getMno(); //반환타입 int
        count = messageRepository.getisread(mno);

        return count;
    }

    //1. 본인이 보낸 메시지 리스트
    public JSONArray getfrommsglist(){ //2.본인(로그인)이 보낸 메시지 리스트
        String mid = getloginmid();
        if(mid == null) return null;

        List<MessageEntity> list =
        memberRepository.findBymid(mid).get().getFromentitylist();

        //json형 변환[js에서 사용하기 위해]
        JSONArray jsonArray = new JSONArray();
        for(MessageEntity msg : list){
            JSONObject object = new JSONObject();
            object.put("msgno", msg.getMsgno());
            object.put("msg", msg.getMsg());
            object.put("to", msg.getToentity().getMid());
            object.put("date", msg.getCreatedate());
            object.put("isread", msg.isIsread()); //읽음 여부
            jsonArray.put(object);
        }
        return jsonArray;
    }


    //2. 본인이 받은 메시지
    public JSONArray gettomsglist(){ //2.본인(로그인)이 받은 메시지 리스트
        String mid = getloginmid();
        if(mid == null) return null;

        List<MessageEntity> list =
                memberRepository.findBymid(mid).get().getFromentitylist();

        //json형 변환[js에서 사용하기 위해]
        JSONArray jsonArray = new JSONArray();
        for(MessageEntity msg : list){
            JSONObject object = new JSONObject();
            object.put("msgno", msg.getMsgno());
            object.put("msg", msg.getMsg());
            object.put("from", msg.getToentity().getMid());
            object.put("date", msg.getCreatedate());
            object.put("isread", msg.isIsread()); //읽음 여부
            jsonArray.put(object);
        }
        return jsonArray;
    }
    //읽음 처리 메소드[수정] 해당 메시지번호 엔티티의 읽음여부 수정
    @Transactional
    public boolean isread( int msgno ){
        //해당 메시지 번호 엔티티의 읽음 여부 수정
        memberRepository.findById(msgno).get().setIsread(true);
        return true;
    }

    //선택된 메세지 삭제
    @Transactional
    public boolean msgdelete(List<Integer> deletelist){

        //1. 반복문 이용한 모든 엔티티 호출
        for(int msgno : deletelist){
            MessageEntity entity = memberRepository.findBymid(msgno).get();
            messageRepository.delete(entity);
        }
        return true;
    }




}
