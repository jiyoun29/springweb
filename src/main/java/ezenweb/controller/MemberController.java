package ezenweb.controller;

import ezenweb.Dto.MemberDto;
import ezenweb.Service.MemberService;
import ezenweb.domain.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //controller와 view의 통신역할
public class MemberController {

    //1. 로그인 페이지 이동 매핑
    @GetMapping("/login") //매핑 걸기
    public String login(){
        return "login";
    }

    //서비스 메모리 할당
    @Autowired
    MemberService memberService;

    //2. 회원가입 페이지 이동 매핑
    @GetMapping("/signup") //매핑 걸기
    public String signup(){

        //dto 생성
        MemberDto memberDto =
        MemberDto.builder()
                .mid("qweqwe")
                .mpw("qweqwe")
                .mname("qweqwe")
                .build();

        //서비스 호출
        memberService.signup(memberDto);
        return "signup";
    }


}
