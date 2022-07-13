package ezenweb.controller;

import ezenweb.Dto.MemberDto;
import ezenweb.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller //controller와 view의 통신역할. 템플릿 영역
@RequestMapping("/member") //이 컨트롤러로 들어오려면 member로 들어와라
public class MemberController {
    //서비스 메모리 할당
    @Autowired //member서비스 객체 선언
    MemberService memberService;


    //4. 아이디/비번 찾기 처리 매핑
    @GetMapping("/find")
    public String find(){
        return "member/find";
    }

    //이메일 인증 여부 확인
    @GetMapping("authmailcheck")
    @ResponseBody
    public int authmailcheck(@RequestParam("mid") String mid){
        int result = memberService.authmailcheck(mid);
        return result;
    }




    //아이디 찾기 oauth2는 회원제공x
    @GetMapping("/idfind")
    @ResponseBody
    public String idfind(@RequestParam("mname") String mname,
                         @RequestParam("memail") String memail
            ){ //변수로 받기

        String idfind = memberService.idfind(mname, memail);
        return idfind;
    }

    //비밀번호 찾기
    @GetMapping("/pwfind")
    @ResponseBody
    public boolean pwfind(@RequestParam("mid") String mid,
                         @RequestParam("memail") String memail
    ){ //변수로 받기
        boolean result = memberService.pwfind(mid, memail);
        return result;
    }

    /* 기본자료형   vs    클래스명 (래퍼클래스)
         int       vs     Integer
      사용 용도는 동일하다.
    * */




    //1. 로그인 페이지 이동 매핑
    @GetMapping("/login") //매핑 걸기
    public String login(){
        return "member/login";
    }
    
    
    
    //회원이 이메일 받았을때 검증 버튼을 누르면 들어오는 매핑
    @GetMapping("/email/{authkey}/{mid}")
    public String signupmail(@PathVariable("authkey") String authkey, @PathVariable("mid") String mid){
        //PathVariable : 경로상(url) 변수 요청

        //이메일 검증 처리
        boolean result = memberService.authsuccess(authkey, mid);
        if(result) { //화면 전환
            return "/member/authsuccess";
        } else {return ""; }
    }
    


    //3. 로그인 처리 매핑
//    @PostMapping("/login")
//    @ResponseBody
//    public boolean save(@RequestParam("mid") String mid ,@RequestParam("mpw") String mpw){
////        System.out.println(mid+ mpw);
//        //dto가 아닌 개별적으로 받아옴
//        return memberService.login(mid, mpw);
//    }

    //시큐리티 사용 시 필요 없음

    //4.로그아웃 처리 매핑
//    @GetMapping("/logout")
//    public String logout(){
//        memberService.logout();
////        return "main"; //타임리프 반환
//        return "redirect:/"; //URL 이동
//    }



    //5. 회원 정보 페이지 이동 매핑
    @GetMapping("/update")
    public String update() {

        return "member/update";
    }


    //6. 회원 수정 처리 매핑
    @PutMapping("/update")
    @ResponseBody
    public boolean update(@RequestParam("mname") String mname){
        return memberService.update(mname);
    }



    //7.
    @GetMapping("/info")
    public String info() {
        return "member/info";
    }

    //8.
    @GetMapping("/myroom")
    public String myroom() {
        return "member/myroom";
    }


    //9. 삭제 페이지 이동 매핑
    @GetMapping("/delete")
    public String delete(){return "member/delete";}

    //10. 삭제
    @DeleteMapping("/delete")
    @ResponseBody
    public boolean delete(@RequestParam("mpw") String mpw){
        return memberService.delete(mpw);
    }


    ////////////////////////////////////////////////////////

    //접속링크
    //2.회원가입 페이지 이동 매핑
    @GetMapping("/signup") //<-url
    public String signup(){ //페이지 열기
        return "member/write"; //write html 불러오기(파일명)
    }


    //3. 회원가입 처리 매핑
    @PostMapping("/signup") //매핑 걸기
    @ResponseBody //객체 반환
    public boolean save(MemberDto memberDto){ //자동주입
        //서비스 호출
        boolean result = memberService.signup(memberDto);
        System.out.println("컨트롤러이메일");
        return result; //성공, 실패를 반환
    }



    //////////////쪽지
    @GetMapping("/getisread")
    @ResponseBody
    public Integer getisread(){
        return memberService.getisread();
    }







}
