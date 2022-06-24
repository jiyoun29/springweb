package ezenweb.Service;

import ezenweb.Dto.LoginDto;
import ezenweb.Dto.MemberDto;
import ezenweb.domain.member.MemberEntity;
import ezenweb.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService implements UserDetailsService {
                        //UserDetailsService 인터페이스 [ 추상 메소드 존재 ]

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
    
    

    //2. 회원가입처리 메소드
    public boolean signup(MemberDto memberDto){
        //dto -> entity [ 이유 : Dto는 DB로 들어갈 수 없다 ]
        MemberEntity memberEntity = memberDto.toentitiy();
            //dto에 builder 만들었으므로 dto에서 호출한다.

        //entity 저장
        memberRepository.save(memberEntity);

        //저장여부 판단
        if(memberEntity.getMno() < 1){ //getmno를 위해 entity에 getter을 추가한다.
            return false;
        } else { return true; }

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
















}
