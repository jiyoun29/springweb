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
    @Autowired
    MemberService memberService;


    //1. 로그인 페이지 이동 매핑
    @GetMapping("/login") //매핑 걸기
    public String login(){
        return "/member/login";
    }


    //3. 로그인 처리 매핑
    @PostMapping("/login")
    @ResponseBody
    public boolean save(@RequestParam("mid") String mid ,@RequestParam("mpw") String mpw){

//        System.out.println(mid+ mpw);
        //dto가 아닌 개별적으로 받아옴
        return memberService.login(mid, mpw);

    }


    //4.로그아웃 처리 매핑
    @GetMapping("/logout")
    public String logout(){
        memberService.logout();
//        return "main"; //타임리프 반환
        return "redirect:/"; //URL 이동
    }



    //5. 회원 정보 페이지 이동 매핑
    @GetMapping("/update")
    public String update() {

        return "/member/update";
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
        return "/member/info";
    }

    //8.
    @GetMapping("/myroom")
    public String myroom() {
        return "/member/myroom";
    }


    //9. 삭제 페이지 이동 매핑
    @GetMapping("/delete")
    public String delete(){return "/member/delete";}

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
        return "/member/write"; //write html 불러오기(파일명)
    }


    //3. 회원가입 처리 매핑
    @PostMapping("/signup") //매핑 걸기
    @ResponseBody //객체 반환
    public boolean save(MemberDto memberDto){ //자동주입
        //서비스 호출
        boolean result = memberService.signup(memberDto);
        return result; //성공, 실패를 반환
    }


}
